import java.util.Random;
import java.util.Stack;

// Feruz Karimov


public class Treap<E extends Comparable<E>> {

    private static class Node<E> {
        // Data fields
        public E data; // key for the search
        public int priority; // random heap priority
        public Node <E> left;
        public Node <E> right;

        public Node(E data,int priority) {

            if (data == null) {
                throw new IllegalArgumentException();
            }
            else {
                this.data = data;
                this.priority = priority;
                this.left = null;
                this.right = null;
            }
        }


        Node<E> rotateRight(){
            // method performs right rotation
        	if (this.left == null) {
        		return this;
        		
        	}else {
        		Node<E> newPar = this.left;
                Node<E> temp = this.left.right;
                this.left = temp;
                newPar.right = this;
                return newPar;
        	}
        }

        Node<E> rotateLeft(){
            // method performs left rotation
        	if (this.right == null) {
        		return this;
        		
        	}else {
        		Node<E> nPar = this.right;
                Node<E> temp = this.right.left;
                this.right = temp;
                nPar.left = this;
                return nPar;	
        	} 
        }

        public String toString() {

        	return "(key = " + data.toString() + ", priority = " + priority + ")";
        }
    }

    private Random priorityGenerator;
    private Node<E> root;

    public Treap() {

        priorityGenerator = new Random();
        root = null;
    }

    public Treap(long seed) {

        priorityGenerator = new Random(seed);
        root = null;
    }

    public boolean add(E key) {

        return add(key, priorityGenerator.nextInt());
    }

    public boolean add(E key, int priority) {
        // if treap is empty, create a new node
        if (root == null) {
            root = new Node<E>(key, priority);
            return true;

        } else {
            // otherwise insert nodes as in binary tree
            // add key in correct positions, comparing nodes to root,changing references of nodes
            // to organize treap based on priorities, call reheap method
            // return true if addition was successful
            Stack<Node<E>> stack = new Stack<Node<E>>();
            Node<E> tempN = root;
            while (tempN != null) {
                int check = tempN.data.compareTo(key);
                if (check == 0) {
                    return false;
                } else {
                    if (check < 0) {
                        stack.push(tempN);
                        if (tempN.right == null) {
                            tempN.right = new Node<E>(key, priority);
                            reheap(tempN.right, stack);
                            return true;
                        } else {
                            tempN = tempN.right;
                        }
                    } else {
                        stack.push(tempN);
                        if (tempN.left == null) {
                            tempN.left = new Node<E>(key, priority);
                            reheap(tempN.left, stack);
                            return true;
                        } else {
                            tempN = tempN.left;
                        }
                    }
                }
            }
            return false;
        }
    }

    public void reheap(Node<E> temp, Stack<Node<E>> stack) {
        // helper method reorganize treap based on priorities
        // if node has higher priorities than parent rotate data
        while (!stack.isEmpty()) {
            Node<E> parent = stack.pop();
            if (parent.priority < temp.priority) {
                if (parent.data.compareTo(temp.data) > 0) {
                    temp = parent.rotateRight();
                } else {
                    temp = parent.rotateLeft();
                }
                if (!stack.isEmpty()) {
                    if (stack.peek().left == parent) {
                        stack.peek().left = temp;
                    } else {
                        stack.peek().right = temp;
                    }
                } else {
                    this.root = temp;
                }
            } else {
                break;
            }
        }
    }

    public boolean delete(E key) {
        // if key is not found or treap is empty return false
        // otherwise delete node with given key
        if (find(key) == false || root == null) {
            return false;

        } else {
            root = delete(key, root);
            return true;
        }
    }

    private Node<E> delete(E key, Node<E> tempRoot) {
        // deletes nodes with given key from a treap
        // recursively deletes nodes based on cases:
        // 1. if node has no children
        // 2. if left child has less priority than the right child
        // 3. if node to be deleted has only one child
        if (tempRoot == null) {
            return tempRoot;
        } else {
            if (tempRoot.data.compareTo(key) < 0) {
                tempRoot.right = delete(key, tempRoot.right);
            } else {
                if (tempRoot.data.compareTo(key) > 0) {
                    tempRoot.left = delete(key, tempRoot.left);
                } else {
                    if (tempRoot.right == null) {
                        tempRoot = tempRoot.left;
                    } else if (tempRoot.left == null) {
                        tempRoot = tempRoot.right;
                    } else {
                        if (tempRoot.right.priority < tempRoot.left.priority) {
                            tempRoot = tempRoot.rotateRight();
                            tempRoot.right = delete(key, tempRoot.right);
                        } else {
                            tempRoot = tempRoot.rotateLeft();
                            tempRoot.left = delete(key, tempRoot.left);
                        }
                    }
                }
            }
        }
        return tempRoot;
    }

    private boolean find(Node<E> root, E key) {
    	
    	// check if treap is empty
    	if (root == null) {
			return false;
			
		} else {
			
			int compare = key.compareTo(root.data);
			// traverse through tree, if key is found return true
			if (compare == 0) {
				return true;
				
			} else if (compare < 0) {
				return find(root.left, key);
				
			} else {
				return find(root.right, key);
			}
		}
    }

    public boolean find(E key) {
    	// check if key is null
        if (key == null) {
            throw new NullPointerException("Cannot be null");
        }
        return find(root, key);
    }

    public String toString() {
    	// method carries out preorder traversal of the tree
    	// returns a representation of the nodes as a string
        StringBuilder sb = new StringBuilder();
        preOrderTraverse(root, 1, sb);
        return sb.toString();

    }


    private void preOrderTraverse(Node<E> node, int depth, StringBuilder sb) {
    	// representation each node with key and priority, left and right child
    	// if left or right child does not exist string return key,priority null next right or left child
        for (int i = 1; i < depth; i++) {
            sb.append("  ");
        }
        if (node == null) {
            sb.append("null\n");
            
        } else {
        	
        	sb.append(node.toString());
			sb.append("\n");
			preOrderTraverse(node.left, depth + 1, sb);
			preOrderTraverse(node.right, depth + 1, sb);
        }
    }


    public static void main(String[] args) {
    	
        Treap<Integer> testTree = new Treap<Integer>();
        //testing add method
        testTree.add(4,19);
        testTree.add(2,31);
        testTree.add(6,70);
        testTree.add(1,84);
        testTree.add(3,12);
        testTree.add(5,83);
        testTree.add(7,26);
              
        // testing delete and find methods
        System.out.println("Treap is:");
        System.out.println(testTree.toString());
        
        System.out.println("Delete key 1: "+ testTree.delete(1)); 
        System.out.println("Find key 6: "+ testTree.find(6)); 
        System.out.println("After deleting key 6: " + testTree.delete(6));
        System.out.println();
        System.out.println(testTree.toString());
        
        System.out.println();
        System.out.println("Find key 2: "+ testTree.find(2));
        System.out.println();
        System.out.println(testTree.toString());
        System.out.println("After deleting key 4: " + testTree.delete(4));
        System.out.println();
        System.out.println(testTree.toString());
        
        System.out.println("After deleting key 7: " + testTree.delete(7));
        System.out.println("After deleting key 3: " + testTree.delete(3));
        System.out.println();
        System.out.println(testTree.toString());
        
        System.out.println("After adding key 7: " + testTree.add(7,26));
        System.out.println("After adding key 6: " + testTree.add(6,77));
        System.out.println("After adding key 10: " + testTree.add(10,87));
        System.out.println("After adding key 5: " + testTree.add(5,93));
        
        System.out.println();
        System.out.println("Find key 7: "+ testTree.find(7));
        System.out.println("Find key 6: "+ testTree.find(6));
        System.out.println("Find key 10: "+ testTree.find(10));
        System.out.println("Find key 45: "+ testTree.find(45));
        System.out.println();
        System.out.println(testTree.toString());

    }
}
