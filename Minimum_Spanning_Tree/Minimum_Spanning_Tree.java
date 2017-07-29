/**********************************************************************
 * Class Name : Minimum_Spanning_Tree
 * Details : This class is used to identify a Minimum Spanning Tree from a given sets of points forming a Graph.
 * Created By : Sumedh Ambokar
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

public class Minimum_Spanning_Tree {
	
	public static HashMap<Integer, ArrayList<Node>> AdjacentNodeMapper; // Map used to store list of adjacent vertices of a particular vertex 
	public static HeapClass GraphHeap;	// HeapClass object used as a heap priority queue
	public static Integer NumberOfNodes; // Number of vertices in the given graph
	public static HashMap<Integer, Integer> IndexMap; // Map used to store heap index of a vertex

	/*	
	 * Method Name: Main
	 * Description:	Executable method. 
	 * Parameters: String[] args
	 * Return Type: void
	 */
	public static void main(String[] args) {
		AdjacentNodeMapper = new HashMap<Integer, ArrayList<Node>>();
		IndexMap = new HashMap<Integer, Integer>();
		try {
			readFile();
			initiateGraphHeap();
			runPrimsAlgorithm();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/*	
	 * Method Name: readFile
	 * Description:	Method used to read Graph from the given file
	 * Parameters: void
	 * Return Type: void
	 */
	static void readFile(){
		try{
			BufferedReader buffrdr = new BufferedReader(new InputStreamReader
						(new FileInputStream(new File("input.txt")), "UTF8"));
			String line = "";
			Boolean first = true;
			while((line = buffrdr.readLine()) != null){
				if(first){ // for the first line of input file
					first = false;
					NumberOfNodes = Integer.valueOf(line.split(" ")[0]);
					GraphHeap = new HeapClass(NumberOfNodes);
				}else{
					addNewNode(Integer.valueOf(line.split(" ")[0]),Integer.valueOf(line.split(" ")[1]),Integer.valueOf(line.split(" ")[2]));
				}
			}
			buffrdr.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*	
	 * Method Name: runPrimsAlgorithm
	 * Description:	Method used to execute Prims algorithm on the given Graph using Heap as its priority queue
	 * Parameters: void
	 * Return Type: void
	 */
	static void runPrimsAlgorithm(){
		try{
			ArrayList<String> MSTList = new ArrayList<String>();
			Integer totalweight = 0;
			Node currentNode;
			while(GraphHeap.getHeapSize() > 0){
				currentNode = GraphHeap.extract_min();	// get the node which is at minimum distance from the set of visited vertices
				if(currentNode.parentNode != 0){ // skip for first vertex
					// add an edge to the minimum spanning tree
					MSTList.add(String.valueOf(currentNode.parentNode) + " " + String.valueOf(currentNode.value));
					totalweight += currentNode.keyValue;
				}
				if(AdjacentNodeMapper.get(currentNode.value) != null){
					for(Node adjacentNode : AdjacentNodeMapper.get(currentNode.value)){
						// update the key values of adjacent vertices for the current extracted vertex
						GraphHeap.decrease_key(adjacentNode.value, adjacentNode.keyValue, currentNode.value);
					}
				}
			}
			printMST(MSTList,totalweight);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/*	
	 * Method Name: addNewNode
	 * Description: Method used to populate Map storing adjacent vertices 
	 * Parameters: Integer value, Integer adjacentValue, Integer edge
	 * Return Type: void
	 */
	static void addNewNode(Integer value, Integer adjacentValue, Integer edge){
		if(AdjacentNodeMapper.containsKey(value)){ 
			AdjacentNodeMapper.get(value).add(new Node(adjacentValue, edge, value));
		}else{
			AdjacentNodeMapper.put(value, new ArrayList<Node>());
			AdjacentNodeMapper.get(value).add(new Node(adjacentValue, edge, value));
		}
		if(AdjacentNodeMapper.containsKey(adjacentValue)){
			AdjacentNodeMapper.get(adjacentValue).add(new Node(value, edge, value));
		}else{
			AdjacentNodeMapper.put(adjacentValue, new ArrayList<Node>());
			AdjacentNodeMapper.get(adjacentValue).add(new Node(value, edge, value));
		}
	}
	
	/*	
	 * Method Name: initiateGraphHeap
	 * Description: Method used to set initial values of all the vertices in the heap to infinity(Integer.MAX_VALUE)
	 * Parameters: void
	 * Return Type: void
	 */
	static void initiateGraphHeap(){
		for(int i=1; i <= NumberOfNodes;i++){
			GraphHeap.insert(i, Integer.MAX_VALUE, 0);
			IndexMap.put(i, i - 1);
		}
	}
	
	
	/*	
	 * Method Name: printMST
	 * Description: Method used to print the minimum spanning tree in the required format
	 * Parameters: ArrayList<String> MSTList, Integer totalweight
	 * Return Type: void
	 */
	static void printMST(ArrayList<String> MSTList, Integer totalweight){
		try{
			Writer Filewtr = new BufferedWriter(new OutputStreamWriter
						(new FileOutputStream(new File("output.txt")), "UTF8"));
			Filewtr.write(String.valueOf(totalweight + "\n"));
			for(String str : MSTList){
				Filewtr.write(str + "\n");
			}
			Filewtr.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*	
	 * Class Name: HeapClass
	 * Description: Private class used for creating a heap priority queue
	 */
	static class HeapClass{
		ArrayList<Node> heap;
		Integer heapSize;
		
		
		/*	
		 * Method Name: HeapClass
		 * Description: Constructor used to initiate heap 
		 * Parameters: Integer size
		 * Return Type: void
		 */
		public HeapClass(Integer size)
		{
			heap = new ArrayList<Node>(size);
			heapSize = 0;
		}

		/*	
		 * Method Name: insert
		 * Description: Method used to insert a vertex in a heap
		 * Parameters: Integer value, Integer keyValue, Integer parentNode
		 * Return Type: void
		 */
		public void insert(Integer value, Integer keyValue, Integer parentNode) {
			heap.add(new Node(value,keyValue,parentNode));
			heapify_up(heap.size());
			heapSize++;
		}

		/*	
		 * Method Name: extract_min
		 * Description: Method used to extract the root of a heap
		 * Parameters: void
		 * Return Type: void
		 */
		public Node extract_min() {
			try{
				Node returnValue = heap.get(0);
				// Change indexes of the extracted and the new first vertex
				IndexMap.put(heap.get(heap.size() - 1).value, 0); 
				IndexMap.put(heap.get(0).value, -1);
				heap.set(0, heap.get(heap.size() - 1));  // Set the last vertex as the first vertex in the heap
				heap.remove(heap.size() - 1); // remove the last vertex from heap
				heapify_down(1); // update positions of all the vertices in heap from the root
				heapSize--;
				return returnValue;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}

		/*	
		 * Method Name: decrease_key
		 * Description: Method used to update weight against a vertex in a heap
		 * Parameters: Integer value, Integer keyValue, Integer parentNode
		 * Return Type: void
		 */
		public void decrease_key(Integer value, Integer keyValue, Integer parentNode){
			try{
				Integer index = IndexMap.get(value);
				if(index < heapSize && index != -1)
					if(heap.get(index).keyValue >= keyValue){ // decrease key value only if the current value is greater than the new value
						heap.get(index).keyValue = keyValue;
						heap.get(index).parentNode = parentNode;
						heapify_up(index + 1);	// Update the positions of the vertices in a heap from the index of the updated vertex
					}
			}catch(Exception e){
				System.out.println(e);
				e.printStackTrace();
			}
		}
			
		
		/*	
		 * Method Name: heapify_up
		 * Description: Method used to update the position of vertices in a heap from a particular index using down top approach
		 * Parameters: Integer index
		 * Return Type: void
		 */
		void heapify_up(Integer index) {
			try{
				Node swapTemp;
				while (index > 1) {
					// index - 1 instead of index while setting values in heap so that 0th position of an array will be utilized
					if (heap.get(index/2 - 1).keyValue > heap.get(index - 1).keyValue) { 
						// perform swap if the child vertex has lesser key value than the parent vertex and change the index in IndexMap
						swapTemp = heap.get(index - 1);
						heap.set(index - 1, heap.get(index/2 - 1));
						IndexMap.put(heap.get(index/2 - 1).value, index - 1);
						heap.set(index/2 - 1, swapTemp);
						IndexMap.put(swapTemp.value, index/2 - 1);
					}
					index = index/2; // set index to the parent vertex of the current vertex
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		/*	
		 * Method Name: heapify_down
		 * Description: Method used to update the position of vertices in a heap from a particular index using top down approach
		 * Parameters: Integer index
		 * Return Type: void
		 */
		void heapify_down(Integer index) {
			try{
				Integer indexCompare;
				Node swapTemp;
				while (2*index <= heap.size()) {
					// index - 1 instead of index while setting values in heap so that 0th position of an arrayList will be utilized
					// identify the child vertex with greater key value
					if (2*index == heap.size() || heap.get(2*index - 1).keyValue < heap.get(2*index).keyValue) 
						indexCompare = 2*index - 1;
					else 
						indexCompare = 2 * index;
					if (heap.get(index - 1).keyValue > heap.get(indexCompare).keyValue) {
						// perform swap if the child vertex has lesser key value than the parent vertex and change the index in IndexMap
						swapTemp = heap.get(index - 1);
						heap.set(index - 1, heap.get(indexCompare));
						IndexMap.put(heap.get(indexCompare).value, index - 1);
						heap.set(indexCompare, swapTemp);
						IndexMap.put(swapTemp.value, indexCompare);
						index = indexCompare + 1; // set index to the child vertex of the current vertex
					}
					else
						break;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		/*	
		 * Method Name: getHeapSize
		 * Description: Method used to Return size of heap
		 * Parameters: void
		 * Return Type: void
		 */
		Integer getHeapSize(){
			return heapSize;
		}
	}
	
	/*	
	 * Class Name: Node
	 * Description: Class used to store information related to a given vertex
	 */
	static class Node{
		Integer value;
		Integer keyValue;
		Integer parentNode;
		
		/*	
		 * Method Name: Node
		 * Description: Constructor of Node class
		 * Parameters: Integer value, Integer keyValue, Integer parentNode
		 * Return Type: void
		 */
		public Node(Integer value, Integer keyValue, Integer parentNode){
			this.value = value;
			this.keyValue = keyValue;
			this.parentNode = parentNode;
		}
	}
}