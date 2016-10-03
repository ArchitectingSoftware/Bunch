/****
 *
 *	$Log: GridBagConstraints2.java,v $
 *	Revision 3.0  2002/02/03 18:41:51  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/07/26 22:46:10  bsmitc
 *	*** empty log message ***
 *	
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *	
 *
 */
/*
 * Copyright (c) 1997-1998 Borland International, Inc. All Rights Reserved.
 * 
 * This SOURCE CODE FILE, which has been provided by Borland as part
 * of a Borland product for use ONLY by licensed users of the product,
 * includes CONFIDENTIAL and PROPRIETARY information of Borland.  
 *
 * USE OF THIS SOFTWARE IS GOVERNED BY THE TERMS AND CONDITIONS 
 * OF THE LICENSE STATEMENT AND LIMITED WARRANTY FURNISHED WITH
 * THE PRODUCT.
 *
 * IN PARTICULAR, YOU WILL INDEMNIFY AND HOLD BORLAND, ITS RELATED
 * COMPANIES AND ITS SUPPLIERS, HARMLESS FROM AND AGAINST ANY CLAIMS
 * OR LIABILITIES ARISING OUT OF THE USE, REPRODUCTION, OR DISTRIBUTION
 * OF YOUR PROGRAMS, INCLUDING ANY CLAIMS OR LIABILITIES ARISING OUT OF
 * OR RESULTING FROM THE USE, MODIFICATION, OR DISTRIBUTION OF PROGRAMS
 * OR FILES CREATED FROM, BASED ON, AND/OR DERIVED FROM THIS SOURCE
 * CODE FILE.
 */
//--------------------------------------------------------------------------------------------------
// Copyright (c) 1996-1998 Borland International, Inc. All Rights Reserved.
//--------------------------------------------------------------------------------------------------

package bunch;				//borland.jbcl.layout;

import java.awt.*;

public class GridBagConstraints2 extends GridBagConstraints implements java.io.Serializable
{
  public GridBagConstraints2(int gridx, int gridy, int gridwidth, int gridheight,
                             double weightx, double weighty, int anchor, int fill,
                             Insets insets, int ipadx, int ipady) {
    this.gridx = gridx;
    this.gridy = gridy;
    this.gridwidth = gridwidth;
    this.gridheight = gridheight;
    this.fill = fill;
    this.ipadx = ipadx;
    this.ipady = ipady;
    this.insets = insets;
    this.anchor  = anchor;
    this.weightx = weightx;
    this.weighty = weighty;
  }

  public String toString() {
    return  ": " + gridx + "," + gridy+ "," + gridwidth + "," + gridheight;    //NORES
  }
}
