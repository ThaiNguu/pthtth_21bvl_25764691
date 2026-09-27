package exercise;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class HostUriInspector {

    public static void main(String[] args) {

        // Kiểm tra đủ 2 tham số hay chưa
        if (args.length != 2) {
            System.out.println(
                    "Usage: java exercise.HostUriInspector <hostname> <URI>");
            return;
        }

        String hostname = args[0];
        String uriText = args[1];

        // 1. Xử lý hostname
        try {
            System.out.println("=== HOST INFORMATION ===");
            System.out.println("Hostname: " + hostname);

            InetAddress[] addresses =
                    InetAddress.getAllByName(hostname);

            for (InetAddress address : addresses) {

                System.out.println("IP: "
                        + address.getHostAddress());

                if (address instanceof Inet4Address) {
                    System.out.println("Type: IPv4");
                } else if (address instanceof Inet6Address) {
                    System.out.println("Type: IPv6");
                }

                System.out.println("Loopback: "
                        + address.isLoopbackAddress());

                System.out.println("Site local: "
                        + address.isSiteLocalAddress());

                System.out.println();
            }

        } catch (UnknownHostException e) {

            System.out.println(
                    "Khong phan giai duoc hostname: "
                            + hostname);
        }

        // 2. Xử lý URI
        try {
            URI uri = new URI(uriText);

            System.out.println("=== URI INFORMATION ===");

            System.out.println("Scheme: "
                    + uri.getScheme());

            System.out.println("Host: "
                    + uri.getHost());

            System.out.println("Port: "
                    + uri.getPort());

            System.out.println("Path: "
                    + uri.getPath());

            System.out.println("Query: "
                    + uri.getQuery());

            System.out.println("Fragment: "
                    + uri.getFragment());

        } catch (URISyntaxException e) {

            System.out.println(
                    "URI khong hop le: " + uriText);
        }
    }
}