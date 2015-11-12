package org.neil.satisfiability;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by neilsharpe on 11/11/15.
 */
public class PartialSolutionTest {
  @Test
  public void testAdd(){
    Map<Long,Boolean> c = new HashMap<>();
    c.put(1l,true);
    c.put(2l,false);

    List<Clause> clauses = new ArrayList<>();
    clauses.add(Clause.of(c));

    SatisfiabilityProblem sp = SatisfiabilityProblem.of(clauses);
    PartialSolution solution = new PartialSolution(sp);
    assertTrue(solution.solution.isEmpty());
  }
  @Test
  public void testGetChoiceBreakdownAllAvailable(){
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
    PartialSolution solution = new PartialSolution(sp);
    PartialSolution.ChoiceBreakDown choiceBreakDown = solution.getChoiceBreakDown();
    assertTrue(choiceBreakDown.noSolution.isEmpty());
    assertTrue(choiceBreakDown.oneSolution.isEmpty());
    assertEquals(3,choiceBreakDown.twoSolution.size());

    solution.add(1l,true);
    choiceBreakDown = solution.getChoiceBreakDown();
    assertEquals(0,choiceBreakDown.noSolution.size());
    assertEquals(0,choiceBreakDown.twoSolution.size());
    assertEquals(1,choiceBreakDown.oneSolution.size());
  }
}
