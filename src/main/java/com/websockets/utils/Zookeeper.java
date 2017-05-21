/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 **/

package com.websockets.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;

public class Zookeeper {

    private String host;
    private int port;
    private CuratorFramework curator;
    private final int UPPER_BOUND = 65000;
    private final int LOWER_BOUND = 10000;
    private final int servicePort = new Random().nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;

    public Zookeeper(String host, int port) {
        this.host = host;
        this.port = port;
        curator = CuratorFrameworkFactory.newClient(host + ":" + port, new RetryNTimes(3, 3000));
    }

    public void register() {
        try {
            final ServiceInstance<Object> serviceInstance = ServiceInstance.builder()
                    .uriSpec(new UriSpec("{scheme}://{address}:{port}")).address(getIp()).port(getPort())
                    .name("kafka-ws").build();

            final ServiceDiscovery<Object> serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class)
                    .basePath("com.packt.microservices").client(curator).thisInstance(serviceInstance).build();

            curator.start();
            serviceDiscovery.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    serviceDiscovery.unregisterService(serviceInstance);
                } catch (Exception e) {
                    System.err.println("Error while unregistering service in Zookeeper. Details: " + e.getMessage());
                    e.printStackTrace();
                }
            }));
        } catch (Exception e) {
            System.err.println("Error while registering service in Zookeeper. Details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void unregister() {
        try {
            curator.close();
        } catch (Exception e) {
        }
    }

    private String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("Error while finding local IP.Using localhost for now. Details: " + e.getMessage());
            e.printStackTrace();
            return "localhost";
        }
    }

    public int getPort() {
        try {
            return servicePort;
        } catch (Exception e) {
            System.err.println("Error while finding service port. Using default port 8080. Details: " + e.getMessage());
            e.printStackTrace();
            return 8080;
        }
    }
}
