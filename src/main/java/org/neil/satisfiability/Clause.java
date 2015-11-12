package org.neil.satisfiability;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is immutable and thread safe
 *
 * A clause is a collection of bits where when given a single solution if any bit matches the solution the clause is considered solved.
 *
 * This class provides a variety of tools for manipulation or inquries on a given clause.
 *
 * @author "neil sharpe"
 * @see {@link SatisfiabilityProblem}
 */
public class Clause {
  private final Map<Long,Boolean> clause;
  public static final Clause EMPTY = of(Collections.emptyMap());

  private Clause(Map<Long,Boolean> c){
    this.clause = Collections.unmodifiableMap(new HashMap<>(c));
  }

  public Boolean canStillSolve(Map<Long,Boolean> values){
    if(!values.keySet().containsAll(clause.keySet())){
      return true;
    }
    Optional<Map.Entry<Long,Boolean>> optional = clause.entrySet().stream()
            .filter(x->values.get(x.getKey()).equals(x.getValue()))
            .findAny();
    return optional.isPresent();
  }

  public Boolean field(Long l){
    return clause.get(l);
  }

  public Clause removeFields(Collection<Long> fields){
    if(!fields.stream()
            .filter(x->clause.containsKey(x))
            .findAny()
            .isPresent()){
      return this;
    }
    return of(clause.entrySet().stream()
            .filter(x->!fields.contains(x.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue)));
  }

  public Boolean isSolved(Map<Long,Boolean> solution){
    return clause.entrySet().stream()
            .filter(x-> x.getValue().equals(solution.get(x.getKey())))
            .findAny().isPresent();
  }

  public Boolean contains(Clause c){
    return clause.entrySet().containsAll(c.clause.entrySet());
  }

  public Boolean isEmpty(){
    return clause.isEmpty();
  }

  public Integer size(){
    return clause.size();
  }

  public Set<Long> fields(){
    return clause.keySet();
  }

  public static Clause of(Map<Long,Boolean> c){
    if(c.isEmpty()){
      return EMPTY;
    }
    return new Clause(c);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Clause clause1 = (Clause) o;
    return Objects.equals(clause, clause1.clause);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clause);
  }
}
