package org.neil.satisfiability;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by neilsharpe on 11/11/15.
 */
public class SatisfiabilityProblemTest {
  @Test
  public void testCanStillSolve(){
    Map<Long,Boolean> c = new HashMap<>();
    c.put(1l,true);
    c.put(2l,false);

    List<Clause> clauses = new ArrayList<>();
    clauses.add(Clause.of(c));

    c.remove(2l);
    c.put(1l,false);
    c.put(3l,true);
    clauses.add(Clause.of(c));
    SatisfiabilityProblem sp = SatisfiabilityProblem.of(clauses);

    c.put(1l,true);
    c.put(2l,true);
    assertTrue(sp.canStillSolve(c));
    c.remove(1l);
    assertTrue(sp.canStillSolve(c));
    c.put(1l,false);
    assertFalse(sp.canStillSolve(c));
  }

  @Test
  public void testIsSolved(){
    Map<Long,Boolean> c = new HashMap<>();
    c.put(1l,true);
    c.put(2l,false);

    List<Clause> clauses = new ArrayList<>();
    clauses.add(Clause.of(c));

    c.remove(2l);
    c.put(1l,false);
    c.put(3l,true);
    clauses.add(Clause.of(c));
    SatisfiabilityProblem sp = SatisfiabilityProblem.of(clauses);

    c.put(1l,true);
    c.put(2l,true);
    assertTrue(sp.isSolved(c));
    c.remove(1l);
    assertFalse(sp.isSolved(c));
    c.put(1l,false);
    assertFalse(sp.isSolved(c));
  }

  @Test
  public void testSimplifyProblem(){
    Map<Long,Boolean> c = new HashMap<>();
    c.put(1l,true);
    c.put(2l,false);

    List<Clause> clauses = new ArrayList<>();
    clauses.add(Clause.of(c));

    c.remove(2l);
    c.put(1l,false);
    c.put(3l,true);
    clauses.add(Clause.of(c));

    SatisfiabilityProblem sp = SatisfiabilityProblem.of(clauses);
    c.clear();
    c.put(1l,true);
    sp = sp.simplifyProblem(c);
    assertEquals(Integer.valueOf(1),sp.numberOfClauses());
    assertTrue(sp.fields().collect(Collectors.toSet()).contains(3l));
  }
}
