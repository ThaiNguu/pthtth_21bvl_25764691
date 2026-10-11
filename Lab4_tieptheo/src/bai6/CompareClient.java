
package bai6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Locale;

public class CompareClient {

    private static final String HOST = "localhost";

    private static final int TCP_PORT = 5007;
    private static final int UDP_PORT = 5008;

    private static final int MESSAGE_SIZE = 100;
    private static final int MESSAGE_COUNT = 1000;
    private static final int RUNS = 5;

    private static final int TCP_TIMEOUT_MS = 3000;
    private static final int UDP_TIMEOUT_MS = 1000;

    public static void main(String[] args) {

        long totalTcpTime = 0;
        long totalUdpTime = 0;

        System.out.println("=== SO SANH TCP VA UDP ===");
        System.out.println("May chu: " + HOST);
        System.out.println(
            "He dieu hanh: " + System.getProperty("os.name"));
        System.out.println(
            "Java: " + System.getProperty("java.version"));
        System.out.println(
            "Kich thuoc moi thong diep: "
            + MESSAGE_SIZE + " byte");
        System.out.println(
            "So thong diep moi luot: " + MESSAGE_COUNT);
        System.out.println("So luot: " + RUNS);
        System.out.println(
            "TCP read timeout: " + TCP_TIMEOUT_MS + " ms");
        System.out.println(
            "UDP receive timeout: " + UDP_TIMEOUT_MS + " ms");
        System.out.println(
            "Phuong phap: do tong thoi gian gui va nhan "
            + "1000 phan hoi.");
        System.out.println();

        try {

            for (int run = 1; run <= RUNS; run++) {

                Result tcp = benchmarkTcp(run);
                totalTcpTime += tcp.elapsedNanos;

                printResult("TCP", run, tcp);

                Result udp = benchmarkUdp(run);
                totalUdpTime += udp.elapsedNanos;

                printResult("UDP", run, udp);

                System.out.println();
            }

            double averageTcp =
                totalTcpTime / (double) RUNS / 1_000_000.0;

            double averageUdp =
                totalUdpTime / (double) RUNS / 1_000_000.0;

            System.out.println("=== KET QUA TRUNG BINH ===");

            System.out.printf(
                Locale.US,
                "TCP: %.3f ms%n", averageTcp);

            System.out.printf(
                Locale.US,
                "UDP: %.3f ms%n", averageUdp);

            System.out.println(
                "Hay so sanh ca 5 luot, khong chi dua vao "
                + "mot lan chay.");

        } catch (IOException e) {

            System.err.println(
                "Khong the hoan thanh phep do: "
                + e.getMessage());

            System.err.println(
                "Hay kiem tra TCP Server va UDP Server "
                + "da duoc khoi dong chua.");
        }
    }

    private static byte[] createMessage(int sequence) {

        byte[] message = new byte[MESSAGE_SIZE];

        ByteBuffer.wrap(message).putInt(sequence);

        Arrays.fill(
            message, Integer.BYTES,
            message.length, (byte) 'A');

        return message;
    }

    private static Result benchmarkTcp(int run)
            throws IOException {

        int correct = 0;
        int errors = 0;
        int sent = 0;

        try (
            Socket socket = new Socket(HOST, TCP_PORT);

            DataOutputStream out = new DataOutputStream(
                socket.getOutputStream());

            DataInputStream in = new DataInputStream(
                socket.getInputStream())
        ) {

            socket.setTcpNoDelay(true);
            socket.setSoTimeout(TCP_TIMEOUT_MS);

            byte[] response = new byte[MESSAGE_SIZE];

            long start = System.nanoTime();

            for (int i = 0; i < MESSAGE_COUNT; i++) {

                byte[] request = createMessage(
                    (run - 1) * MESSAGE_COUNT + i);

                out.write(request);
                out.flush();
                sent++;

                in.readFully(response);

                if (Arrays.equals(request, response)) {
                    correct++;
                } else {
                    errors++;
                }
            }

            long elapsed = System.nanoTime() - start;

            return new Result(
                sent, correct, errors, elapsed);
        }
    }

    private static Result benchmarkUdp(int run)
            throws IOException {

        int correct = 0;
        int errors = 0;
        int sent = 0;
        int consecutiveTimeouts = 0;

        InetAddress address = InetAddress.getByName(HOST);

        try (DatagramSocket socket = new DatagramSocket()) {

            socket.setSoTimeout(UDP_TIMEOUT_MS);

            byte[] responseBuffer = new byte[MESSAGE_SIZE];

            DatagramPacket response = new DatagramPacket(
                responseBuffer, responseBuffer.length);

            long start = System.nanoTime();

            for (int i = 0; i < MESSAGE_COUNT; i++) {

                byte[] requestData = createMessage(
                    (run - 1) * MESSAGE_COUNT + i);

                DatagramPacket request = new DatagramPacket(
                    requestData,
                    requestData.length,
                    address,
                    UDP_PORT);

                socket.send(request);
                sent++;

                response.setLength(responseBuffer.length);

                try {

                    socket.receive(response);

                    if (response.getLength() == MESSAGE_SIZE
                            && Arrays.equals(
                                requestData, responseBuffer)) {

                        correct++;
                        consecutiveTimeouts = 0;

                    } else {
                        errors++;
                        consecutiveTimeouts = 0;
                    }

                } catch (SocketTimeoutException e) {

                    errors++;
                    consecutiveTimeouts++;

                    if (consecutiveTimeouts >= 3) {
                        System.out.println(
                            "UDP timeout 3 lan lien tiep; "
                            + "dung luot nay de tranh cho qua lau.");
                        break;
                    }
                }
            }

            long elapsed = System.nanoTime() - start;

            return new Result(
                sent, correct, errors, elapsed);
        }
    }

    private static void printResult(
            String protocol, int run, Result result) {

        System.out.printf(
            Locale.US,
            "Luot %d - %s: gui %d/%d, "
            + "phan hoi dung %d, loi/timeout %d, "
            + "thoi gian %.3f ms%n",
            run,
            protocol,
            result.sent,
            MESSAGE_COUNT,
            result.correct,
            result.errors,
            result.elapsedNanos / 1_000_000.0);
    }

    private static class Result {

        int sent;
        int correct;
        int errors;
        long elapsedNanos;

        Result(
                int sent,
                int correct,
                int errors,
                long elapsedNanos) {

            this.sent = sent;
            this.correct = correct;
            this.errors = errors;
            this.elapsedNanos = elapsedNanos;
        }
    }
}
