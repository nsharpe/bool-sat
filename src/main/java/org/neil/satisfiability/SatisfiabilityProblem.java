package org.neil.satisfiability;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by neilsharpe on 11/11/15.
 */
public class SatisfiabilityProblem {
  private final Set<Clause> clauses;

  public static SatisfiabilityProblem of(Stream<Clause> clauses){
    return new SatisfiabilityProblem(clauses.collect(Collectors.toSet()));
  }

  public static SatisfiabilityProblem of(Collection<Clause> clauses){
    return new SatisfiabilityProblem(new HashSet<>(clauses));
  }

  private SatisfiabilityProblem(Set<Clause> clauses) {
    this.clauses = Collections.unmodifiableSet(clauses);
  }

  public Boolean canStillSolve(Map<Long,Boolean> solution){
    return !clauses.stream()
            .filter(x->!x.canStillSolve(solution))
            .findAny()
            .isPresent();
  }

  public SatisfiabilityProblem removeField(Collection<Long> fields){
    return of(removeField(fields,clauses.stream())
            .collect(Collectors.toSet()));
  }

  public Boolean isSolved(Map<Long,Boolean> solution){
    return !clauses.stream()
            .filter(x->!x.isSolved(solution))
            .findAny()
            .isPresent();
  }

  private Stream<Clause> removeField(Collection<Long> fields,Stream<Clause> clauseStream){
    return clauseStream.map(x -> x.removeFields(fields))
            .filter(x->!x.isEmpty());
  }

  public SatisfiabilityProblem simplifyProblem(Map<Long,Boolean> solution){
    if(!canStillSolve(solution)){
      return null;
    }
    return of(removeField(
            solution.keySet(),
            clauses.stream().filter(x->!x.isSolved(solution))));
  }

  public Integer numberOfClauses(){
    return clauses.size();
  }

  public Stream<Long> fields(){
    return clauses.stream()
            .flatMap(x->x.fields().stream())
            .distinct();
  }

  public Stream<Long> fields(Map<Long,Boolean> solution){
    return clauses.stream()
            .filter(x->!x.isSolved(solution))
            .flatMap(x->x.fields().stream().filter(y->!solution.containsKey(y)))
            .distinct();
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
