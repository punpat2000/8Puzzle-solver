import java.util.ArrayList;

public class BFS8Puzzle {
	
	public static final String DEFAULT_SOLUTION = "123456780";
	
	public class Node implements Comparable<Node> {
		private State s;
		private String ans;
		public Node(State s,String ans) {
			this.s = s;
			this.ans = ans;
		}
		public State getState() {
			return this.s;
		}
		public String getAnswer() {
			return ans;
		}
		@Override
		public int compareTo(Node o) {
			return this.s.distance() - o.getState().distance();
		}
	}
	
	public class State {
		private String s;
		private int size;
		private int currentBlank;
		public int distance() {
			int d = 0;
			for(int i = 0; i < size; i++)
				for(int j = 0; j < size; j++) {
					final int n = s.charAt(i*size + j) - '0'; 
					if(n!=0)
						d += Math.abs((n-1)/size-i) + Math.abs((n-1)%size-j);
				}
			return d;
		}
		public State(String s,int size) {
			this.s = s;
			this.size = size;
			currentBlank = s.indexOf("0");
		}
		public boolean canMoveUp() {
			if(currentBlank < size) 
				return false;
			return true;
		}
		public boolean canMoveDown() {
			if(size*size-currentBlank > size) 
				return true;
			return false;
		}
		public boolean canMoveLeft() {
			if(currentBlank % size == 0)
				return false;
			return true;
		}
		public boolean canMoveRight() {
			if((currentBlank+1) % size == 0) 
				return false;
			return true;
		}
		private void swap(int i,int j) {
			String p1 = s.substring(i,i+1);
			String p2 = s.substring(j,j+1);
			s = s.substring(0,i)+p2+s.substring(i+1,j)+p1+s.substring(j+1);
		}
		public void moveUp() {
			swap(currentBlank-size,currentBlank);
			currentBlank -= size;
		}
		public void moveRight() {
			swap(currentBlank,currentBlank+1);
			currentBlank++;
		}
		public void moveLeft() {
			swap(currentBlank-1,currentBlank);
			currentBlank--;
		}
		public void moveDown() {
			swap(currentBlank,currentBlank+size);
			currentBlank += size;
		}
		public void show() {
			for(int i=0;i<size;i++) {
				for(int j=0;j<size;j++) {
					System.out.print(s.substring(i*size+j,i*size+j+1)+" ");
				}
				System.out.println();
			}
			System.out.println();
		}
		public String getString() {
			return s;
		}
	}
	public interface FringeAbstract {
		public void add(Node n) throws Exception;
		public Node removeFront() throws Exception;
		public boolean isEmpty();
		public void showAll() throws Exception;
		public int getNumAdded();
		public int getNumRemoved();
	}
	
	public class FringePriorityQ implements FringeAbstract {
		PriorityQ<Node> f = new Heap<Node>();
		private int numAdded = 0, numRemoved = 0;
		@Override
		public void add(Node n) throws Exception {
			numAdded++;
			f.add(n);
		}
		@Override
		public Node removeFront() throws Exception {
			this.numRemoved++;
			return f.pop();
		}
		@Override
		public boolean isEmpty() {
			return f.isEmpty();
		}
		@Override
		public void showAll() throws Exception {
			while(!f.isEmpty())
				f.pop().getState().show();
		}
		@Override
		public int getNumAdded() {
			return this.numAdded;
		}
		@Override
		public int getNumRemoved() {
			return this.numRemoved;
		}
	}
	
	public class Fringe implements FringeAbstract {
		ArrayList<Node> f = new ArrayList<Node>();
		private int numAdded = 0, numRemoved = 0;
		public void add(Node n) throws Exception {
			numAdded++;
			f.add(n);
		}
		public Node removeFront() throws Exception {
			numRemoved++;
			return f.remove(0);
		}
		public boolean isEmpty() {
			return f.isEmpty();
		}
		public void showAll() throws Exception {
			for(int i=0;i<f.size();i++) {
				f.get(i).getState().show();
			}
		}
		public int getNumAdded() {
			return this.numAdded;
		}
		public int getNumRemoved() {
			return this.numRemoved;
		}
	}
	
	public static void insertAll(FringeAbstract f,State s,String ans,int size) throws Exception {
		BFS8Puzzle t = new BFS8Puzzle();
		if(s.canMoveUp()) {
			State tempState = t.new State(s.getString(),size);
			tempState.moveUp();
			f.add(t.new Node(tempState,ans+"U"));
		}
		if(s.canMoveDown()) {
			State tempState = t.new State(s.getString(),size);
			tempState.moveDown();
			f.add(t.new Node(tempState,ans+"D"));
		}
		if(s.canMoveRight()) {
			State tempState = t.new State(s.getString(),size);
			tempState.moveRight();
			f.add(t.new Node(tempState,ans+"R"));
		}	
		if(s.canMoveLeft()) {
			State tempState = t.new State(s.getString(),size);
			tempState.moveLeft();
			f.add(t.new Node(tempState,ans+"L"));
		}	
	}
	public static void main(String[] args) throws Exception {
		BFS8Puzzle t = new BFS8Puzzle();
		int size=3;
		State s = t.new State("162573048",size);
		
		s.show();
		FringeAbstract f = t.new Fringe();
		f.add(t.new Node(s,""));
		System.out.println("Estimated Steps: " + s.distance());
		long start, end;
		start = System.nanoTime();
		while(true) {
			if(f.isEmpty()) {
				System.out.println("Fail");
				break;
			}
			Node front =  f.removeFront();
			if(front.getState().getString().equals(DEFAULT_SOLUTION)) {
				System.out.println(front.getAnswer());
				break;
			}
			insertAll(f,front.getState(),front.getAnswer(),size);
		}
		end = System.nanoTime();
		System.out.println("time: "+(end - start));
		System.out.println("Added Nodes: " + f.getNumAdded());
		System.out.println("Removed Nodes: " + f.getNumRemoved());
		f = t.new FringePriorityQ();
		f.add(t.new Node(s,""));
		start = System.nanoTime();
		while(true) {
			if(f.isEmpty()) {
				System.out.println("Fail");
				break;
			}
			Node front =  f.removeFront();
			if(front.getState().getString().equals("123456780")) {
				System.out.println(front.getAnswer());
				break;
			}
			insertAll(f,front.getState(),front.getAnswer(),size);
		}
		end = System.nanoTime();
		System.out.println("time: "+(end - start));
		System.out.println("Added Nodes (with priorityQ): " + f.getNumAdded());
		System.out.println("Remmoved Nodes (with priorityQ): " + f.getNumRemoved());
	}
}
