/****
 *
 *	$Log: GATournamentMethod.java,v $
 *	Revision 3.0  2002/02/03 18:41:49  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/07/26 22:46:09  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

/**
 * A Genetic Algorithm that usues tournament selection, as described
 * in Golberg's book, plus elitism.
 *
 * @author Brian Mitchell
 */
public
class GATournamentMethod
  extends GAMethod
{
protected int[] tempArray_d;

public
GATournamentMethod()
{
}

public
void
init()
{
  setIncrementCounter(1);
  setInitCounter(0);
  setMaxCounter(currentPopulation_d.length);
  tempArray_d = new int[currentPopulation_d[0].getClusters().length];
}

/**
 * Selection, crossover and reproduction (including mutation) for tournament
 * selection.
 */
public
void
selectReproduceCrossAndMutate(int pos)
{
  //selection
  Graph parent1 = currentPopulation_d[(int)((currentPopulation_d.length-1) * randomGenerator_d.nextFloat())];
  Graph parent2 = currentPopulation_d[(int)((currentPopulation_d.length-1) * randomGenerator_d.nextFloat())];

  int[] p1c = parent1.getClusters();
  int[] p2c = parent2.getClusters();

  if (parent1.getObjectiveFunctionValue()
        < parent2.getObjectiveFunctionValue()) {
    p1c = parent2.getClusters();
    p2c = parent1.getClusters();
  }

  int[] np1c = newPopulation_d[pos].getClusters();

  System.arraycopy(p1c, 0, np1c, 0, p1c.length);

  //crossover
  if (randomGenerator_d.nextFloat()<crossoverThreshold_d) {
    int crossp = (int)(randomGenerator_d.nextFloat() * (np1c.length-1));
    cross(np1c, p2c, crossp);
  }

  //mutation
  for (int i=0; i<np1c.length; ++i) {
    if (randomGenerator_d.nextFloat()< mutationThreshold_d) {
      mutate(np1c, i);
    }
  }
}

/**
 * Mutate the selected individual
 */
public
void
mutate(int[] c, int pos)
{
    c[pos] = (int)(randomGenerator_d.nextFloat() * (c.length-1));
}

/**
 * Perform crossover of c1c with c2c at the crossp position
 */
public
void
cross(int[] c1c, int[] c2c, int crossp)
{
  System.arraycopy(c2c, crossp, c1c, crossp, c1c.length-crossp);
}
}
