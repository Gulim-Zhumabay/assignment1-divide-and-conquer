import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new Experiment().runAll("results/results.csv");
        System.out.println("ready, results are in results/results.csv");
    }
}