/* This program is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the 
 * License, or (at your option) any later version
                                
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 * Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, 
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA                                                                  

 *       Marine Dumousseau and Nicolas Le Novere                                                   
 *       EMBL-EBI, neurobiology computational group,                          
 *       Cambridge, UK. e-mail: melting-forum@googlegroups.com        */

package examples;

import java.util.ArrayList;
import java.util.Properties;

/**
 * This class exists to test the different hydroxyadenine methods.
 */
public class MainTestHydroxyadenine {

	public static void main(String[] args) {
		ArrayList<String> hydroxyadenineMethods = new ArrayList<String>();
		hydroxyadenineMethods.add("sug01");

		Properties hydroxyadenineSequences = MainTest.loadSequencesTest("src/examples/test/hydroxyadenineSequences.txt");
		
		System.out.print("\n\n melting.sequences \t TmExp \t sug01 \n");

		MainTest.displayResultsWithComplementarySequence(hydroxyadenineSequences, hydroxyadenineMethods, "dnadna", "Na=1", "0.0001", "-ha");
	}

}
