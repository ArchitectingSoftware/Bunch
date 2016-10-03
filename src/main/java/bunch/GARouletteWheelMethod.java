/****
 *
 *	$Log: GARouletteWheelMethod.java,v $
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
 * A Genetic Algorithm that usues roulette wheel selection, as described
 * in Golberg's book, plus elitism.
 *
 * @author Brian Mitchell
 */
public
class GARouletteWheelMethod
  extends GAMethod
{
protected int[] tempArray_d;
protected int shakeUpCount_d;
public
GARouletteWheelMethod()
{
}

public
void
init()
{
  setIncrementCounter(2);
  setInitCounter(0);
  setMaxCounter(currentPopulation_d.length-1);
  tempArray_d = new int[currentPopulation_d[0].getClusters().length];
  shakeUpCount_d = -1500;
}

/**
 * Selection, crossover and reproduction (including mutation) for roulette
 * wheel selection.
 */
public
void
selectReproduceCrossAndMutate(int pos)
{
  Graph parent1 = null, parent2 = null;

  //selection
  double partsum=0.0;
  double rand1 = (sumOFValue_d * randomGenerator_d.nextFloat());
  double rand2 = (sumOFValue_d * randomGenerator_d.nextFloat());

  for (int i=0; i<fitnessArray_d.length; ++i) {
    if(parent1==null&&rand1<=fitnessArray_d[i]) {
      parent1 = currentPopulation_d[i];
    }
    if (parent2==null&&rand2<=fitnessArray_d[i]) {
      parent2 = currentPopulation_d[i];
    }
    if (parent1 != null && parent2 != null) {
      break;
    }
  }

  int[] p1c = parent1.getClusters();
  int[] p2c = parent2.getClusters();

  int[] np1c = newPopulation_d[pos].getClusters();
  int[] np2c = newPopulation_d[pos+1].getClusters();
  System.arraycopy(p1c, 0, np1c, 0, p1c.length);
  System.arraycopy(p2c, 0, np2c, 0, p2c.length);

  //crossover
  if (randomGenerator_d.nextFloat()<crossoverThreshold_d) {
    int crossp = (int)(randomGenerator_d.nextFloat() * (np1c.length-1));
    cross(np1c, np2c, crossp);
  }

  //mutation
  for (int i=0; i<np1c.length; ++i) {
    if (randomGenerator_d.nextFloat()< mutationThreshold_d) {
      mutate(np1c, i);
//      mutCounter_d = 0;
    }
  }

  for (int i=0; i<np2c.length; ++i) {
    if (randomGenerator_d.nextFloat()< mutationThreshold_d) {
      mutate(np2c, i);
    }
  }
}

/**
 * Update the data structures to deal with the new fitness value
 */
protected
void
processFitnessValues()
{
  sumOFValue_d = 0.0;
  int nummax = 0;
  for (int i=0; i<fitnessArray_d.length; ++i) {
    if (fitnessArray_d[i] == maxOFValue_d) {
      ++nummax;
    }
  }

  if (nummax > (currentPopulation_d.length/2)) {
    nummax = (int)(((float)nummax)*0.6);

    for (int i=0; i<fitnessArray_d.length && nummax > 0; ++i) {
      if (fitnessArray_d[i] == maxOFValue_d) {
        currentPopulation_d[i].shuffleClusters();
        currentPopulation_d[i].calculateObjectiveFunctionValue();
        fitnessArray_d[i] = ((currentPopulation_d[i].getObjectiveFunctionValue() + 1.0) / 2.0);
        --nummax;
      }
      if (fitnessArray_d[i] > maxOFValue_d) {
        maxOFValue_d = fitnessArray_d[i];
        if (currentPopulation_d[i].getObjectiveFunctionValue()
                > getBestGraph().getObjectiveFunctionValue()) {
          setBestGraph(currentPopulation_d[i].cloneGraph());
        }
      }
      if (minOFValue_d > fitnessArray_d[i]) {
        minOFValue_d = fitnessArray_d[i];
      }
    }
  }

  double prior = getScaledFitness(fitnessArray_d[0]);
  fitnessArray_d[0] = prior;
  for (int i=1; i<fitnessArray_d.length; ++i) {
    fitnessArray_d[i] = getScaledFitness(fitnessArray_d[i])+prior;
		prior = fitnessArray_d[i];
  }
  sumOFValue_d=fitnessArray_d[fitnessArray_d.length-1];
}

/**
 * Scale the difference to range between 0 and 1
 */
public
double
getScaledFitness(double fit)
{
    double mult = 1.0;
    double cdiff = fit-minOFValue_d;
    double odiff = maxOFValue_d-minOFValue_d;
    if (odiff == 0.0) {
      odiff = 1.0;
      cdiff = 1.0;
    }
    mult = 1.0 * (cdiff/odiff);
    return mult;
}

/**
 * Determine a mutation position, if a mutation operation is needed
 */
public
void
mutate(int[] c, int pos)
{
    c[pos] = (int)(randomGenerator_d.nextFloat() * (c.length-1));
}

public
void
shakePopulation()
{
}

/**
 * Crossover the population member c1c with c2c at the crossp position
 */
public
void
cross(int[] c1c, int[] c2c, int crossp)
{
  System.arraycopy(c1c, crossp, tempArray_d, crossp, c1c.length-crossp);
  System.arraycopy(c2c, crossp, c1c, crossp, c1c.length-crossp);
  System.arraycopy(tempArray_d, crossp, c2c, crossp, c1c.length-crossp);
}
}
