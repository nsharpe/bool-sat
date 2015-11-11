package org.neil.satisfiability;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by neilsharpe on 11/11/15.
 */
public class SatisfiabilityProblem {
  private final Set<Clause> clauses;

  private SatisfiabilityProblem(Collection<Clause> clauses) {
    this.clauses = Collections.unmodifiableSet(new HashSet<>(clauses));
  }

  public Boolean canStillSolve(Map<Long,Boolean> solution){
    return !clauses.stream()
            .filter(x->!x.canStillSolve(solution))
            .findAny()
            .isPresent();
  }

  public SatisfiabilityProblem removeField(Collection<Long> fields){
    return of(clauses.stream()
            .map(x -> x.removeFields(fields))
            .filter(x->!x.isEmpty())
            .collect(Collectors.toSet()));
  }

  public static SatisfiabilityProblem of(Collection<Clause> clauses){
    return new SatisfiabilityProblem(clauses);
  }

  public Boolean isSolved(Map<Long,Boolean> solution){
    return !clauses.stream()
            .filter(x->!x.isSolved(solution))
            .findAny()
            .isPresent();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SatisfiabilityProblem that = (SatisfiabilityProblem) o;
    return Objects.equals(clauses, that.clauses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clauses);
  }
}
