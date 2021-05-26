package experimental;

import events.SchlechtesExperiment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Executioner {
    private final boolean resetExperiment;
    private final List<Experiment> experiments;
    private final List<Long> duration = new ArrayList<>();

    private Executioner(List<Experiment> experiments, boolean resetExperiment) {
        this.experiments = experiments;
        this.resetExperiment = resetExperiment;
    }

    public Executioner executeExperiments() {
        for (Experiment e : this.experiments) {
            long ts = System.nanoTime();
            e.executeExperiment();
            this.duration.add((System.nanoTime() - ts));
            if (this.resetExperiment) System.gc();
        }
        return this;
    }

    public void analyze() {
        System.out.println("Times:");
        this.duration.forEach(longus -> System.out.println((longus / 1e6d) + " ms"));

        System.out.println("Average for experiments");
        System.out.println(this.duration.stream().mapToDouble(value -> value / 1e6f).average().orElse(-1d) + " ms");
    }

    public static ExecutionerBuilder builder() {
        return new ExecutionerBuilder();
    }

    public static class ExecutionerBuilder {
        private boolean resetExperiment;
        private final List<Experiment> experiments = new ArrayList<>();

        public ExecutionerBuilder() {

        }

        public ExecutionerBuilder resetExperiment() {
            this.resetExperiment = true;
            return this;
        }

        public ExecutionerBuilder experiment(Experiment experiment) {
            this.experiments.add(experiment);
            return this;
        }

        public Executioner build() {
            return new Executioner(this.experiments, this.resetExperiment);
        }
    }

    public static void main(String[] args) {
        Executioner
                .builder()
                .experiment(Experiment
                        .builder()
                        .creator(v -> () -> {
                            SchlechtesExperiment schlechtesExperiment = new SchlechtesExperiment(100);
                            schlechtesExperiment.initialize(0);
                            schlechtesExperiment.evaluate(1);
                        })
                        .iterations(10)
                        .resetIterations()
                        .build())
                .experiment(Experiment
                        .builder()
                        .creator(v -> () -> {
                            SchlechtesExperiment schlechtesExperiment = new SchlechtesExperiment(1000);
                            schlechtesExperiment.initialize(0);
                            schlechtesExperiment.evaluate(1);
                        })
                        .iterations(20)
                        .resetIterations()
                        .build())
                .resetExperiment()
                .build()
                .executeExperiments()
                .analyze();
    }
}

class Experiment {
    public int currentIteration;
    public final int iterations;
    private final boolean resetIterations;
    private final Predicate<Experiment> condition;
    private final Function<Void, ExecuteMe> creator;


    private Experiment(Function<Void, ExecuteMe> creator, int iterations, boolean resetIterations, Predicate<Experiment> condition) {
        this.creator = creator;
        this.iterations = iterations;
        this.resetIterations = resetIterations;
        this.condition = condition;
    }

    public void executeExperiment() {
        while (this.condition.test(this)) {
            this.creator.apply(null).execute();
            if (this.resetIterations) System.gc();
            this.currentIteration++;
        }
    }


    public static ExperimentBuilder builder() {
        return new ExperimentBuilder();
    }

    public static class ExperimentBuilder {
        private boolean resetIterations;
        private int iterations;
        private Function<Void, ExecuteMe> creator;
        private Predicate<Experiment> predicate = e -> true;

        private ExperimentBuilder() {

        }

        public ExperimentBuilder addCondition(Predicate<Experiment> predicate) {
            this.predicate = this.predicate.and(predicate);
            return this;
        }

        public ExperimentBuilder resetIterations() {
            this.resetIterations = true;
            return this;
        }

        public ExperimentBuilder iterations(int iterations) {
            this.predicate = this.predicate.and(e -> e.currentIteration < e.iterations);
            this.iterations = iterations;
            return this;
        }

        public ExperimentBuilder creator(Function<Void, ExecuteMe> creator) {
            this.creator = creator;
            return this;
        }

        public Experiment build() {
            if (this.creator == null) throw new IllegalArgumentException("Creator undefined!");
            if (this.iterations < 1) throw new IllegalArgumentException("Iterations negative!");
            return new Experiment(this.creator, this.iterations, this.resetIterations, this.predicate);
        }
    }

}
