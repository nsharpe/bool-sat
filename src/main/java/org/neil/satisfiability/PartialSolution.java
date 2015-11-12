package org.neil.satisfiability;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is not a thread safe class.
 * <p>
 * This is a mutable class.
 * <p>
 * Created by neilsharpe on 11/11/15.
 */
public class PartialSolution {
  private final SatisfiabilityProblem problem;
  public final Map<Long, Boolean> solution;

  public PartialSolution(SatisfiabilityProblem problem) {
    this.problem = problem;
    solution = new HashMap<>();
  }

  public PartialSolution(PartialSolution toCopy) {
    this.problem = toCopy.problem;
    solution = new HashMap<>(toCopy.solution);
  }

  public PartialSolution(PartialSolution toCopy, Long field, Boolean choice) {
    this.problem = toCopy.problem;
    solution = new HashMap<>(toCopy.solution);
    add(field,choice);
  }

  public Boolean canStillSolve() {
    return problem.canStillSolve(solution);
  }

  public Boolean isSolved() {
    return problem.isSolved(solution);
  }

  public Boolean canAdd(Long field, Boolean b) {
    solution.put(field, b);
    Boolean toReturn = problem.canStillSolve(solution);
    solution.remove(field);
    return toReturn;
  }

  public void add(Long field, Boolean b) {
    solution.put(field, b);
  }

  public ChoiceBreakDown getChoiceBreakDown() {
    Map<Long, Set<Long>> countBreakDown =
            getChoices().collect(
                    Collectors.groupingBy(
                            x -> getPosibilities(x).count(), Collectors.toSet()));
    return new ChoiceBreakDown(
            countBreakDown.get(0l),
            countBreakDown.get(1l),
            countBreakDown.get(2l));
  }

  public Stream<Long> getChoices() {
    return problem.fields(solution).filter(x -> !solution.containsKey(x));
  }

  public Stream<Boolean> getPosibilities(Long field) {
    Boolean isTrueAllowed = canAdd(field, true);
    Boolean isFalseAllowed = canAdd(field, false);
    if (isTrueAllowed) {
      if (isFalseAllowed) {
        return Stream.of(true, false);
      }
      return Stream.of(true);
    }
    if (isFalseAllowed) {
      return Stream.of(false);
    }
    return Stream.empty();
  }

  public class ChoiceBreakDown {
    public final Set<Long> noSolution;
    public final Set<Long> oneSolution;
    public final Set<Long> twoSolution;

    public ChoiceBreakDown(Set<Long> noSolution, Set<Long> oneSolution, Set<Long> twoSolution) {
      noSolution = noSolution == null ? Collections.emptySet() : noSolution;
      oneSolution = oneSolution == null ? Collections.emptySet() : oneSolution;
      twoSolution = twoSolution == null ? Collections.emptySet() : twoSolution;
      this.noSolution = Collections.unmodifiableSet(noSolution);
      this.oneSolution = Collections.unmodifiableSet(oneSolution);
      this.twoSolution = Collections.unmodifiableSet(twoSolution);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PartialSolution solution1 = (PartialSolution) o;
    return Objects.equals(solution, solution1.solution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(solution);
  }
}
