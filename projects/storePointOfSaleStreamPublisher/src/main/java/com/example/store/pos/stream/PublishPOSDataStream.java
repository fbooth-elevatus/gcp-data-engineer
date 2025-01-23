package com.example.store.pos.stream;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PublishPOSDataStream {

    // Logger instance
    private static final Logger logger = LoggerFactory.getLogger(PublishPOSDataStream.class);

    // GCP project and Pub/Sub topic configuration
    private static final String PROJECT_ID = "your-project-id";
    private static final String TOPIC_ID = "pos-transactions";

    // Number of stores and transaction frequency
    private static final int NUM_STORES = 5;
    private static final int TRANSACTIONS_PER_MINUTE = 600; // 100 transactions/store/minute

    private static final Random RANDOM = new Random();
    private static final Gson GSON = new Gson();

    // Map to track the transaction counter for each store
    private static final Map<String, Integer> storeTransactionCounters = new HashMap<>();

    public static void main(String[] args) {
        Publisher publisher = null;
        try {
            // Initialize Pub/Sub publisher
            String topicName = String.format("projects/%s/topics/%s", PROJECT_ID, TOPIC_ID);
            publisher = Publisher.newBuilder(topicName).build();

            // Initialize transaction counters for each store
            initializeStoreTransactionCounters();

            logger.info("Starting to publish {} transactions per minute to topic: {}", TRANSACTIONS_PER_MINUTE, TOPIC_ID);

            // Publish transactions
            for (int i = 0; i < TRANSACTIONS_PER_MINUTE; i++) {
                String transaction = generateTransaction();
                ByteString messageData = ByteString.copyFromUtf8(transaction);

                PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                        .setData(messageData)
                        .build();

                publisher.publish(pubsubMessage);

                logger.debug("Published transaction: {}", transaction);

                // Simulate a transaction every 100ms to match the frequency
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (Exception e) {
            logger.error("Error occurred while publishing transactions: ", e);
        } finally {
            if (publisher != null) {
                try {
                    publisher.shutdown();
                    publisher.awaitTermination(1, TimeUnit.MINUTES);
                } catch (Exception e) {
                    logger.error("Error shutting down publisher: ", e);
                }
            }
        }
    }

    // Initialize transaction counters for each store
    private static void initializeStoreTransactionCounters() {
        for (int i = 1; i <= NUM_STORES; i++) {
            storeTransactionCounters.put("store_" + i, 0);
        }
        logger.info("Initialized transaction counters for {} stores", NUM_STORES);
    }

    // Method to generate a random transaction
    private static String generateTransaction() {
        String[] storeIds = {"store_1", "store_2", "store_3", "store_4", "store_5"};
        String[] loyaltyIds = {"loyalty_123", "loyalty_456", "loyalty_789", "loyalty_012", "loyalty_345"};
        String[] productIds = {"prod_101", "prod_102", "prod_103", "prod_104", "prod_105"};

        // Select a random store and loyalty ID
        String storeId = storeIds[RANDOM.nextInt(storeIds.length)];
        String loyaltyId = loyaltyIds[RANDOM.nextInt(loyaltyIds.length)];

        // Randomize the transaction type (sale or return)
        boolean isReturn = RANDOM.nextBoolean();
        String transactionType = isReturn ? "return" : "sale";

        // Generate a unique transaction ID for the store
        int transactionCounter = storeTransactionCounters.get(storeId) + 1;
        storeTransactionCounters.put(storeId, transactionCounter);
        String transactionId = storeId + "_txn_" + transactionCounter;

        // Generate a random number of products for this transaction
        int numProducts = RANDOM.nextInt(5) + 1; // Between 1 and 5 products
        List<Product> products = new ArrayList<>();

        double totalAmount = 0;
        for (int i = 0; i < numProducts; i++) {
            String productId = productIds[RANDOM.nextInt(productIds.length)];
            int quantity = RANDOM.nextInt(3) + 1; // Quantity between 1 and 3
            double price = RANDOM.nextDouble() * 50 + 1; // Price between $1 and $50
            double amount = quantity * price;

            // Add to product list
            products.add(new Product(productId, quantity, price));

            // Update total amount (negative for returns)
            totalAmount += isReturn ? -amount : amount;
        }

        String timestamp = java.time.Instant.now().toString();

        // Create a transaction object
        Transaction transaction = new Transaction(
                storeId,
                transactionId,
                transactionType,
                loyaltyId,
                products,
                totalAmount,
                timestamp
        );

        // Convert transaction object to JSON
        return GSON.toJson(transaction);
    }

    // Product class for individual items
    static class Product {
        String product_id;
        int quantity;
        double price;

        public Product(String product_id, int quantity, double price) {
            this.product_id = product_id;
            this.quantity = quantity;
            this.price = price;
        }
    }

    // Transaction class for JSON serialization
    static class Transaction {
        String store_id;
        String transaction_id;
        String transaction_type;
        String loyalty_id;
        List<Product> products;
        double total_amount;
        String timestamp;

        public Transaction(String store_id, String transaction_id, String transaction_type,
                           String loyalty_id, List<Product> products, double total_amount,
                           String timestamp) {
            this.store_id = store_id;
            this.transaction_id = transaction_id;
            this.transaction_type = transaction_type;
            this.loyalty_id = loyalty_id;
            this.products = products;
            this.total_amount = total_amount;
            this.timestamp = timestamp;
        }
    }
}
