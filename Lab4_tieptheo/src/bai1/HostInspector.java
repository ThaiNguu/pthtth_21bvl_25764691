
package bai1;

import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class HostInspector {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println(
                "Cach dung: HostInspector <hostname> <URI>");
            System.out.println(
                "Vi du: localhost https://example.com:443/java?lang=vi#top");
            return;
        }

        String hostname = args[0];
        String uriText = args[1];

        System.out.println("=== THONG TIN HOST ===");
        System.out.println("Hostname: " + hostname);

        try {
            InetAddress[] addresses =
                InetAddress.getAllByName(hostname);

            for (InetAddress address : addresses) {
                String type;

                if (address instanceof Inet4Address) {
                    type = "IPv4";
                } else if (address instanceof Inet6Address) {
                    type = "IPv6";
                } else {
                    type = "Khong xac dinh";
                }

                System.out.println("IP: "
                    + address.getHostAddress());
                System.out.println("Loai dia chi: " + type);
                System.out.println("Canonical: "
                    + address.getCanonicalHostName());
                System.out.println("Loopback: "
                    + address.isLoopbackAddress());
                System.out.println("Site local: "
                    + address.isSiteLocalAddress());
                System.out.println();
            }

        } catch (UnknownHostException e) {
            System.out.println(
                "Khong phan giai duoc hostname: " + hostname);
        }

   
        System.out.println("=== THONG TIN URI ===");

        try {
            URI uri = new URI(uriText);

            System.out.println("URI: " + uriText);
            System.out.println("Scheme: " + uri.getScheme());
            System.out.println("Host: " + uri.getHost());
            System.out.println("Port: " + uri.getPort());
            System.out.println("Path: " + uri.getPath());
            System.out.println("Query: " + uri.getQuery());
            System.out.println("Fragment: " + uri.getFragment());

        } catch (URISyntaxException e) {
            System.out.println("URI khong hop le: "
                + e.getMessage());
        }
    }
}
