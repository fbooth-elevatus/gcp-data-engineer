package com.example.dataflow;

import com.google.gson.Gson;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.transforms.windowing.*;
import org.apache.beam.sdk.values.*;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class PosTransactionAnalytics {

    private static final Logger logger = LoggerFactory.getLogger(PosTransactionAnalytics.class);

    public static void main(String[] args) {
        logger.info("Starting Point of Sale Analytics pipeline.");

        Pipeline pipeline = Pipeline.create();

        pipeline
            .apply("ReadMessages", PubsubIO.readStrings().fromTopic("projects/your-project-id/topics/pos-transactions"))
            .apply("ParseTransactions", ParDo.of(new ParseTransactionFn()))

            // Apply fixed windowing of 1 minute
            .apply("ApplyWindow", Window.<Transaction>into(FixedWindows.of(Duration.standardMinutes(1))))

            // Calculate average sales per store
            .apply("MapStoreToSales", MapElements.into(TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.doubles()))
                .via((Transaction transaction) -> KV.of(transaction.getStoreId(), transaction.getTotalAmount())))
            .apply("CalculateAverageSales", Combine.perKey(new AverageDoubleFn()))
            .apply("LogAverageSales", ParDo.of(new LogResults<>("Average Sales per Store")));

            // FB:  Need to figure out why we can't run this following analytic.
            //
            // Calculate average items per order per store
            // .apply("MapStoreToItems", MapElements.into(TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.integers()))
            //     .via((Transaction transaction) -> KV.of(transaction.getStoreId(), transaction.getNumItems())))
            // .apply("CalculateAverageItems", Combine.perKey(new AverageIntegerFn()))
            // .apply("LogAverageItems", ParDo.of(new LogResults<>("Average Items per Order")));

        pipeline.run().waitUntilFinish();

        logger.info("Pipeline execution completed.");
    }

    static class ParseTransactionFn extends DoFn<String, Transaction> {
        private static final Logger logger = LoggerFactory.getLogger(ParseTransactionFn.class);
        private static final Gson gson = new Gson();

        @ProcessElement
        public void processElement(@Element String message, OutputReceiver<Transaction> out) {
            try {
                Transaction transaction = gson.fromJson(message, Transaction.class);
                if (transaction != null) {
                    out.output(transaction);
                }
            } catch (Exception e) {
                logger.error("Failed to parse message: {}", message, e);
            }
        }
    }

    static class LogResults<K, V> extends DoFn<KV<K, V>, Void> {
        private static final Logger logger = LoggerFactory.getLogger(LogResults.class);
        private final String label;

        public LogResults(String label) {
            this.label = label;
        }

        @ProcessElement
        public void processElement(@Element KV<K, V> element) {
            logger.info("{}: {} = {}", label, element.getKey(), element.getValue());
        }
    }

    /**
     * AverageFn for Integer values.
     */
    static class AverageIntegerFn extends Combine.CombineFn<Integer, AverageIntegerFn.Accumulator, Double> {
        static class Accumulator implements Serializable {
            int sum = 0;
            int count = 0;

            void add(int value) {
                sum += value;
                count++;
            }

            Accumulator merge(Accumulator other) {
                sum += other.sum;
                count += other.count;
                return this;
            }

            double average() {
                return count == 0 ? 0.0 : (double) sum / count;
            }
        }

        @Override
        public Accumulator createAccumulator() {
            return new Accumulator();
        }

        @Override
        public Accumulator addInput(Accumulator accumulator, Integer input) {
            accumulator.add(input);
            return accumulator;
        }

        @Override
        public Accumulator mergeAccumulators(Iterable<Accumulator> accumulators) {
            Accumulator merged = createAccumulator();
            for (Accumulator acc : accumulators) {
                merged.merge(acc);
            }
            return merged;
        }

        @Override
        public Double extractOutput(Accumulator accumulator) {
            return accumulator.average();
        }
    }

    /**
     * AverageFn for Double values.
     */
    static class AverageDoubleFn extends Combine.CombineFn<Double, AverageDoubleFn.Accumulator, Double> {
        static class Accumulator implements Serializable {
            double sum = 0.0;
            long count = 0;

            void add(double value) {
                sum += value;
                count++;
            }

            Accumulator merge(Accumulator other) {
                sum += other.sum;
                count += other.count;
                return this;
            }

            double average() {
                return count == 0 ? 0.0 : sum / count;
            }
        }

        @Override
        public Accumulator createAccumulator() {
            return new Accumulator();
        }

        @Override
        public Accumulator addInput(Accumulator accumulator, Double input) {
            accumulator.add(input);
            return accumulator;
        }

        @Override
        public Accumulator mergeAccumulators(Iterable<Accumulator> accumulators) {
            Accumulator merged = createAccumulator();
            for (Accumulator acc : accumulators) {
                merged.merge(acc);
            }
            return merged;
        }

        @Override
        public Double extractOutput(Accumulator accumulator) {
            return accumulator.average();
        }
    }

    static class Transaction implements Serializable {
        private String storeId;
        private double totalAmount;
        private int numItems;

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public int getNumItems() {
            return numItems;
        }

        public void setNumItems(int numItems) {
            this.numItems = numItems;
        }
    }
}
