package org.neil.satisfiability;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by neilsharpe on 11/11/15.
 */
public class ClauseTest {
  @Test
  public void testCanStillSolveWithSolution(){
    Map<Long,Boolean> m = new HashMap<>();
    m.put(0l,true);
    m.put(1l,false);
    Clause c = Clause.of(m);
    assertTrue(c.canStillSolve(m));

    m.put(0l,false);
    assertTrue(c.canStillSolve(m));

    m.put(1l,true);
    assertFalse(c.canStillSolve(m));
  }

  public void testCanStillSolveNoSolution(){
    Map<Long,Boolean> m = new HashMap<>();
    m.put(0l,true);
    m.put(1l,false);
    Clause c = Clause.of(m);

    assertTrue(c.canStillSolve(Collections.emptyMap()));
    m.clear();
    m.put(2l,true);
    assertTrue(c.canStillSolve(m));
  }

  @Test
  public void testIsSolved(){
    Map<Long,Boolean> m = new HashMap<>();
    m.put(0l,true);
    m.put(1l,false);
    Clause c = Clause.of(m);
    assertTrue(c.isSolved(m));

    m.put(0l,false);
    assertTrue(c.isSolved(m));

    m.put(1l,true);
    assertFalse(c.isSolved(m));
  }

  @Test
  public void testRemoveField(){
    Map<Long,Boolean> m = new HashMap<>();
    m.put(0l,true);
    m.put(1l,false);
    Clause c = Clause.of(m);
    c = c.removeFields(Arrays.asList(new Long[]{0l}));
    assertEquals(Integer.valueOf(1),c.size());
    assertFalse(c.field(1l));
  }
}
