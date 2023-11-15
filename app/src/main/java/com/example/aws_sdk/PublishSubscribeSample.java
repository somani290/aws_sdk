/*package com.example.aws_sdk;

import android.content.res.AssetManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;

import java.io.InputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String CLIENT_ENDPOINT = "afmm5q5xo12t8-ats.iot.us-east-1.amazonaws.com";
    private static final String CLIENT_ID = "iotconsole-73b15b7a-55a1-4bbf-aded-a6289f040f1a";
    private static final String CERTIFICATE_FILE_PATH = "/data/data/com.example.aws_sdk/files/d5b6031eb01445673cce123403ceae24d21d1018d87b8d5a89ba5064f187eb40-certificate.pem.crt"; // Change to your certificate file name
    private static final String PRIVATE_KEY_FILE_PATH = "/data/data/com.example.aws_sdk/files/d5b6031eb01445673cce123403ceae24d21d1018d87b8d5a89ba5064f187eb40-private.pem.key"; // Change to your private key file name

    private AWSIotMqttClient awsIotClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SampleUtil.KeyStorePasswordPair pair = null;
        try {
            pair = SampleUtil.getKeyStorePasswordPair(CERTIFICATE_FILE_PATH, PRIVATE_KEY_FILE_PATH);

            awsIotClient = new AWSIotMqttClient(CLIENT_ENDPOINT, CLIENT_ID, pair.keyStore, pair.keyPassword);

            awsIotClient.setConnectionTimeout(60000);
            try {
                awsIotClient.connect();
            } catch (AWSIotException e) {
                e.printStackTrace();
            }

            String topic = "topic";
            String payload = "any payload";

            awsIotClient.publish(topic, AWSIotQos.QOS0, payload);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }

    }


}*/


/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.example.aws_sdk;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.amazonaws.services.iot.client.AWSIotTopic;


/**
 * This is an example that uses {@link AWSIotMqttClient} to subscribe to a topic and
 * publish messages to it. Both blocking and non-blocking publishing are
 * demonstrated in this example.
 */
/*public class PublishSubscribeSample extends AppCompatActivity {

    private static final String TestTopic = "sdk/test/java";
    private static final AWSIotQos TestTopicQos = AWSIotQos.QOS0;

    private static AWSIotMqttClient awsIotClient;

    public static void setClient(AWSIotMqttClient client) {
        awsIotClient = client;
    }

    public static class BlockingPublisher implements Runnable {
        private final AWSIotMqttClient awsIotClient;

        public BlockingPublisher(AWSIotMqttClient awsIotClient) {
            this.awsIotClient = awsIotClient;
        }

        @Override
        public void run() {
            long counter = 1;

            while (true) {
                String payload = "hello from blocking publisher - " + (counter++);
                try {
                    awsIotClient.publish(TestTopic, payload);
                } catch (AWSIotException e) {
                    System.out.println(System.currentTimeMillis() + ": publish failed for " + payload);
                }
                System.out.println(System.currentTimeMillis() + ": >>> " + payload);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(System.currentTimeMillis() + ": BlockingPublisher was interrupted");
                    return;
                }
            }
        }
    }

    public static class NonBlockingPublisher implements Runnable {
        private final AWSIotMqttClient awsIotClient;

        public NonBlockingPublisher(AWSIotMqttClient awsIotClient) {
            this.awsIotClient = awsIotClient;
        }

        @Override
        public void run() {
            long counter = 1;

            while (true) {
                String payload = "hello from non-blocking publisher - " + (counter++);
                AWSIotMessage message = new NonBlockingPublishListener(TestTopic, TestTopicQos, payload);
                try {
                    awsIotClient.publish(message);
                } catch (AWSIotException e) {
                    System.out.println(System.currentTimeMillis() + ": publish failed for " + payload);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(System.currentTimeMillis() + ": NonBlockingPublisher was interrupted");
                    return;
                }
            }
        }
    }


    private static void initClient(CommandArguments arguments) {
        // Replace these values with your own client ID, endpoint, certificate file path, and private key file path
        String clientId = "iotconsole-73b15b7a-55a1-4bbf-aded-a6289f040f1a";
        String clientEndpoint = "afmm5q5xo12t8-ats.iot.us-east-1.amazonaws.com";


        String certificateFilePath = "/data/data/com.example.aws_sdk/files/d5b6031eb01445673cce123403ceae24d21d1018d87b8d5a89ba5064f187eb40-certificate.pem.crt"; // Change to your certificate file name
         String privateKeyFilePath = "/data/data/com.example.aws_sdk/files/d5b6031eb01445673cce123403ceae24d21d1018d87b8d5a89ba5064f187eb40-private.pem.key";

        if (awsIotClient == null) {
            String algorithm = arguments.get("keyAlgorithm", SampleUtil.getConfig("keyAlgorithm"));

            SampleUtil.KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFilePath, privateKeyFilePath, algorithm);

            awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
        }

        if (awsIotClient == null) {
            // If you have AWS credentials, you can use them here
            String awsAccessKeyId = arguments.get("awsAccessKeyId", SampleUtil.getConfig("awsAccessKeyId"));
            String awsSecretAccessKey = arguments.get("awsSecretAccessKey", SampleUtil.getConfig("awsSecretAccessKey"));
            String sessionToken = arguments.get("sessionToken", SampleUtil.getConfig("sessionToken"));

            if (awsAccessKeyId != null && awsSecretAccessKey != null) {
                awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey, sessionToken);
            }
        }

        if (awsIotClient == null) {
            throw new IllegalArgumentException("Failed to construct client due to missing certificate or credentials.");
        }
    }


  *//*  private static void initClient(CommandArguments arguments) {
        String clientEndpoint = arguments.getNotNull("clientEndpoint", SampleUtil.getConfig("clientEndpoint"));
        String clientId = arguments.getNotNull("clientId", SampleUtil.getConfig("clientId"));

        String certificateFile = arguments.get("certificateFile", SampleUtil.getConfig("certificateFile"));
        String privateKeyFile = arguments.get("privateKeyFile", SampleUtil.getConfig("privateKeyFile"));
        if (awsIotClient == null && certificateFile != null && privateKeyFile != null) {
            String algorithm = arguments.get("keyAlgorithm", SampleUtil.getConfig("keyAlgorithm"));

            SampleUtil.KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile, algorithm);

            awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
        }

        if (awsIotClient == null) {
            String awsAccessKeyId = arguments.get("awsAccessKeyId", SampleUtil.getConfig("awsAccessKeyId"));
            String awsSecretAccessKey = arguments.get("awsSecretAccessKey", SampleUtil.getConfig("awsSecretAccessKey"));
            String sessionToken = arguments.get("sessionToken", SampleUtil.getConfig("sessionToken"));

            if (awsAccessKeyId != null && awsSecretAccessKey != null) {
                awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey,
                        sessionToken);
            }
        }

        if (awsIotClient == null) {
            throw new IllegalArgumentException("Failed to construct client due to missing certificate or credentials.");
        }
    }*//*

    public static void main(String args[]) throws InterruptedException, AWSIotException, AWSIotTimeoutException {
        CommandArguments arguments = CommandArguments.parse(args);
        initClient(arguments);

        awsIotClient.connect();

        AWSIotTopic topic = new TestTopicListener(TestTopic, TestTopicQos);
        awsIotClient.subscribe(topic, true);

        Thread blockingPublishThread = new Thread(new BlockingPublisher(awsIotClient));
        Thread nonBlockingPublishThread = new Thread(new NonBlockingPublisher(awsIotClient));

        blockingPublishThread.start();
        nonBlockingPublishThread.start();

        blockingPublishThread.join();
        nonBlockingPublishThread.join();
    }

}*/






import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.amazonaws.services.iot.client.AWSIotTopic;

public class PublishSubscribeSample extends AppCompatActivity {

    private static final String TestTopic = "mytopic";
    private static final AWSIotQos TestTopicQos = AWSIotQos.QOS0;

    private static AWSIotMqttClient awsIotClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Make sure you have a layout file named activity_main.xml

        // Replace these values with your own client ID, endpoint, certificate file path, and private key file path
        String clientId = "iotconsole-73b15b7a-55a1-4bbf-aded-a6289f040f1a";
        String clientEndpoint = "afmm5q5xo12t8-ats.iot.us-east-1.amazonaws.com";

        String certificateFilePath = "/data/data/com.example.aws_sdk/files/d5b6031eb01445673cce123403ceae24d21d1018d87b8d5a89ba5064f187eb40-certificate.pem.crt"; // Change to your certificate file name
        String privateKeyFilePath = "/data/data/com.example.aws_sdk/files/d5b6031eb01445673cce123403ceae24d21d1018d87b8d5a89ba5064f187eb40-private.pem.key";

        if (awsIotClient == null) {
            String algorithm = SampleUtil.getConfig("keyAlgorithm");

            SampleUtil.KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFilePath, privateKeyFilePath, algorithm);

            awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
        }

        if (awsIotClient == null) {
            String awsAccessKeyId = SampleUtil.getConfig("awsAccessKeyId");
            String awsSecretAccessKey = SampleUtil.getConfig("awsSecretAccessKey");
            String sessionToken = SampleUtil.getConfig("sessionToken");

            if (awsAccessKeyId != null && awsSecretAccessKey != null) {
                awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey, sessionToken);
            }
        }

        if (awsIotClient == null) {
            throw new IllegalArgumentException("Failed to construct client due to missing certificate or credentials.");
        }

        try {
            awsIotClient.connect();
            awsIotClient.setKeepAliveInterval(60); // 60 seconds, for example


            AWSIotTopic topic = new TestTopicListener(TestTopic, TestTopicQos);
            awsIotClient.subscribe(topic, true);

            new BlockingPublisherTask().execute(awsIotClient);
            new NonBlockingPublisherTask().execute(awsIotClient);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }

    private static class BlockingPublisherTask extends AsyncTask<AWSIotMqttClient, Void, Void> {
        @Override
        protected Void doInBackground(AWSIotMqttClient... params) {
            AWSIotMqttClient client = params[0];

            long counter = 1;

            while (true) {
                String payload = "hello from blocking publisher - " + (counter++);
                try {
                    client.publish(TestTopic, payload);
                } catch (AWSIotException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    private static class NonBlockingPublisherTask extends AsyncTask<AWSIotMqttClient, Void, Void> {
        @Override
        protected Void doInBackground(AWSIotMqttClient... params) {
            AWSIotMqttClient client = params[0];

            long counter = 1;

            while (true) {
                String payload = "hello from non-blocking publisher - " + (counter++);
                AWSIotMessage message = new NonBlockingPublishListener(TestTopic, TestTopicQos, payload);
                try {
                    client.publish(message);
                } catch (AWSIotException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}


