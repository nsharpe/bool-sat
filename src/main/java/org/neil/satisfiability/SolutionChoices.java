package org.neil.satisfiability;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Not a thread safe class
 * <p>
 * Created by neilsharpe on 11/11/15.
 */
public class SolutionChoices {

  public List<PartialSolution> getNextStepsOptions(Set<List<PartialSolution>> partialSolutions) {
    if (partialSolutions.isEmpty()) {
      return Collections.emptyList();
    }
    if (partialSolutions.stream().filter(x -> x.isEmpty()).findAny().isPresent()) {
      return Collections.emptyList();
    }
    Optional<List<PartialSolution>> oneSolution =
            partialSolutions.stream()
                    .filter(x -> x.size() == 1).findAny();
    if (oneSolution.isPresent()) {
      return oneSolution.get();
    }

    List<PartialSolution> toReturn = null;
    for (List<PartialSolution> ps : partialSolutions) {
      List<PartialSolution> result =
              getNextStepsOptions(getNextStepsOptions(ps)
                      .collect(Collectors.toSet()));
      if(toReturn==null || toReturn.size()> result.size()){
        toReturn = result;
      }
    }
    return toReturn == null ? Collections.emptyList() : toReturn;
  }

  public Stream<List<PartialSolution>> getNextStepsOptions(List<PartialSolution> partialSolutionStream) {
    return partialSolutionStream.stream()
            .flatMap(x -> getNextStepsOptions(x)).distinct();
  }

  public Stream<List<PartialSolution>> getNextStepsOptions(PartialSolution partialSolution) {
    PartialSolution partialSolutionCopy = new PartialSolution(partialSolution);
    Set<Long> nextChoices = getNextFieldChoices(partialSolutionCopy);
    boolean wasModified = false;
    for (Long l : nextChoices) {
      Stream<Boolean> options = partialSolutionCopy.getPosibilities(l);
      if (options.count() == 0) {
        return Stream.empty();
      }
      if (options.count() == 1) {
        partialSolutionCopy.add(l, options.findAny().get());
        wasModified = true;
      }
    }
    if (wasModified) {
      return Stream.of(Arrays.asList(partialSolutionCopy));
    }
    return getNextFieldChoices(partialSolutionCopy).stream()
            .map(l -> nextSolutions(partialSolutionCopy, l))
            .distinct();
  }

  public static List<PartialSolution> nextSolutions(PartialSolution partialSolution, Long field) {
    return partialSolution.getPosibilities(field)
            .map(x -> new PartialSolution(partialSolution, field, x))
            .collect(Collectors.toList());
  }

  public Set<Long> getNextFieldChoices(PartialSolution partialSolution) {
    PartialSolution.ChoiceBreakDown choiceBreakDown = partialSolution.getChoiceBreakDown();
    if (!choiceBreakDown.noSolution.isEmpty()) {
      return choiceBreakDown.noSolution;
    }
    if (!choiceBreakDown.oneSolution.isEmpty()) {
      return choiceBreakDown.oneSolution;
    }
    return choiceBreakDown.twoSolution;
  }
}
