import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GetConnections {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		//initializations CD= device, in = interface, OP = outgoing port
		ArrayList<String> InterestedDevices = new ArrayList<String>();
		ArrayList<String> AllDevices = new ArrayList<String>();
		ArrayList<ArrayList<String>> AllCD = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> Allin = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> AllOP = new ArrayList<ArrayList<String>>();
		
		
		//get path for folders (insert path for directory containing) 
		String path = "C:/Users/Ishaddad/eclipse-workspace/Physical connectivity/ShowCDPfiles2";
		
		File dir = new File(path);
		File[] directoryListing = dir.listFiles();
		
		for (int i=0; i<directoryListing.length; i++)
		{
			System.out.println(directoryListing[i]);
		}
		
		
		//initialize parameters for outputing data on csv file
		String CSVoutputFile = "physicalConnectivity2.csv";
		PrintWriter pw = new PrintWriter(new File(CSVoutputFile));
        StringBuilder sb = new StringBuilder();
        
        String CSVoutputFile2 = "physicalConnectivity-onlyInterested2.csv";
		PrintWriter pw2 = new PrintWriter(new File(CSVoutputFile2));
        StringBuilder sb2 = new StringBuilder();
        
        
        
        //loop through folders in directory
        for (int i=0; i<directoryListing.length; i++)
        {
        	
        	//some initializations
        	String CD ="";
    		String Int="";
    		String OP="";
    		String line ="";
    		ArrayList<String> instance = new ArrayList<String>();
        	
        	System.out.println();
    		System.out.println();
    		
    		
    		//get folder sub file
    		File subdir = new File(directoryListing[i].toString());
    		File[] subdirectoryListing = subdir.listFiles();
    		
    		String myFile = "";
    		
    		for (int j=0; j<subdirectoryListing.length; j++)
    		{
    			if (subdirectoryListing[j].toString().contains("detail"))
    			{
    				myFile = subdirectoryListing[j].toString();
    			}
    		}
    		
    		
    		// split name of path to sub file to get device name. replace "files2" with last string from the name of your directory containing all the files
    		String [] filenamesegmented = directoryListing[i].toString().split("files2");
    		
    		
    		//get device name
        	String Device = filenamesegmented[filenamesegmented.length-1].substring(1);
        	InterestedDevices.add(Device);
        	
    		//read file line by line
			try (BufferedReader br = new BufferedReader(new FileReader(myFile))) {
				
				while ((line = br.readLine()) != null) {
					
					if (line.startsWith("Device ID:"))
					{
						
						// remove "Device ID: " string
						String CDwithDomain = line.replace("Device ID: ", "");
						
						// remove device extension name if exits
						CD = CDwithDomain.replace(".dca.ae","");
					}
					
					if(Device.contains("76") || Device.contains("65") || Device.contains("68"))
					{
						if (line.startsWith("Interface:"))
						{
							String[] sp = line.split(",");
							
							// remove "Interface: " string
							Int = sp[0].replace("Interface: ", "");

							// remove "  Port ID (outgoing port): " string
							OP = sp[1].replace("  Port ID (outgoing port): ", "");
							
							// add device name, interface, and outgoing port to Arraylist instance
							instance.add(CD + ", " + Int + ", " + OP);
						}	
					}
					else
					{
						if (line.startsWith("Interface:"))
						{
							
							// remove "Interface: " string
							Int = line.replace("Interface: ", "");
						}
						
						if (line.startsWith("Port ID (outgoing port):"))
						{
							// remove "Port ID (outgoing port): " string
							OP = line.replace("Port ID (outgoing port): ", "");
							
							// add device name, interface, and outgoing port to Arraylist instance
							instance.add(CD + ", " + Int + ", " + OP);
						}	
					}
					 
				}
			} catch (IOException e) {
	            e.printStackTrace();    
			}
			
			ArrayList<String> DeviceConnected = new ArrayList<String>();
			ArrayList<String> Interface = new ArrayList<String>();
			ArrayList<String> OutgoingPort = new ArrayList<String>();
			
			
			System.out.println("Device = " + Device + " Size = " + instance.size());
			
			System.out.println();
			System.out.println();
			
			//sort devices to keep similar connected devices grouped
			Collections.sort(instance);
			
			
			//print physical connectivity table for all devices
			for (int j=0; j<instance.size(); j++)
			{
				String[] params = instance.get(j).split(", ");
				DeviceConnected.add(params[0]);
				Interface.add(params[1]);
				OutgoingPort.add(params[2]);
				System.out.println(Device + " (" + Interface.get(j) + ") --------------------- (" + OutgoingPort.get(j) + ") " + DeviceConnected.get(j));
			}
			
			//add devices to nested ArrayList
			AllDevices.add(Device);
			AllCD.add(DeviceConnected);
			Allin.add(Interface);
			AllOP.add(OutgoingPort);
        }
        
        System.out.println();
        System.out.println();
        System.out.println();
        
        //all interested devices
        System.out.println("Interested Devices are:");
        for (int j=0; j<InterestedDevices.size(); j++)
		{
        	System.out.println(InterestedDevices.get(j));
        	
		}
        
        
        //test print to make sure nested ArrayList works
        System.out.println();
        System.out.println();
        System.out.println();
        
        System.out.println("test");
        for (int j=0; j<AllCD.get(0).size(); j++)
		{
        	System.out.println(AllDevices.get(0) + " (" + Allin.get(0).get(j) + ") --------------------- (" + AllOP.get(0).get(j) + ") " + AllCD.get(0).get(j));
        	
		}
        
        
        
        
        //print physical connectivity for all links to a csv file
        for (int i=0; i<AllDevices.size(); i++)
        {
        	sb.append(AllDevices.get(i) + ",");
        	for (int j=0; j<Allin.get(i).size(); j++)
        	{
        		sb.append(Allin.get(i).get(j) + ",");
        		sb.append(AllOP.get(i).get(j) + ",");
        		sb.append(AllCD.get(i).get(j) + ",");
        		sb.append("\n");
        		sb.append(",");
        	}
        		sb.append("\n");
        }
        
        pw.write(sb.toString());
        pw.close();
        
        
        
        
        
        
        
        // print physical connectivity for interested links only      
        for (int i=0; i<AllDevices.size(); i++)
        {
        	sb2.append(AllDevices.get(i) + ",");
        	for (int j=0; j<Allin.get(i).size(); j++)
        	{
        		if(InterestedDevices.contains(AllCD.get(i).get(j)))
        		{
	        		sb2.append(Allin.get(i).get(j) + ",");
	        		sb2.append(AllOP.get(i).get(j) + ",");
	        		sb2.append(AllCD.get(i).get(j) + ",");
	        		sb2.append("\n");
	        		sb2.append(",");
        		}
        	}
        		sb2.append("\n");
        }
        
        pw2.write(sb2.toString());
        pw2.close();
        
        
        
        
      //rank connections per device (how many connections per device to other devices) (helps in drawing a topology)
        ArrayList<String> Connections = new ArrayList<String>();
        for (int i=0; i<AllDevices.size(); i++)
        {
        	Integer count = 0;
        	for (int k=0; k<AllDevices.size(); k++)
        	{
	        	if (AllCD.get(k).contains(AllDevices.get(i)))
	        	{
	        		count ++;
	        	}
        	}
        	Connections.add(count.toString() + " " + AllDevices.get(i) );
        }
        
        Collections.sort(Connections);
        
        System.out.println();
        System.out.println();
        
        for(int i=0; i<AllDevices.size(); i++)
        {
        	System.out.println(Connections.get(i));
        }
        
        System.out.println();
        System.out.println();
        
	}

}
