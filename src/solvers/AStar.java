package solvers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;

import game.*;

public class AStar extends Solver
{
    public AStar(State state) throws IOException, InterruptedException
    {
        super(state);
    }

    public ArrayList<Move> reconstituerChemin(Node n)
    {
        ArrayList<Move> path = new ArrayList<>();
        path.add(n.getMove());
        while (n.getNoeudPrecedent() != null)
        {
            n = n.getNoeudPrecedent();
            path.add(n.getMove());
        }
        Collections.reverse(path);
        return path;
    }

    public boolean isInterestingNode(PriorityQueue<Node> openList, HashSet<Node> closedList, Node n)
    {
        if (!(closedList.isEmpty()))
                    {
                        for (Node nodeClose : closedList)
                        {
                            if (nodeClose.equals(n))
                            {
                                return false;
                            }
                        }
                       for (Node nodeOpen : openList)
                       {
                            if (n.equals(nodeOpen))
                            {
                                if (nodeOpen.cout > n.cout)
                                {
                                    return false;
                                }
                            }
                        }
                    }
        return true;
    }

    public Node getBestPath()
    {
        int cout = 1;
        Node startNode = new Node(manhattanDistance(this.posActiveRobot, this.posActiveGoal) + cout, cout, this.initialState, null, null);
        PriorityQueue<Node> openList = new PriorityQueue<Node>(new NodeComparator());
        openList.add(startNode);
        HashSet<Node> closeList = new HashSet<>();

        while (!(openList.isEmpty()))
        {
            Node n = openList.poll();
            cout ++;

            if (n.getState().Get_Robot()[n.getState().getActiveGoal()][0] == this.posActiveGoal[0] && n.getState().Get_Robot()[n.getState().getActiveGoal()][1] == this.posActiveGoal[1])
            {
                return n;
            }

            for (int i = 0; i <= 3; i++)
            {
                for (Move move : n.getState().getMove(i))
                {
                    State s;

                    try {
                        s = n.getState().getClone();
                        s.play(move, i);
                    }
                    catch (Exception e) {return null;}

                    int[] newPosRobot = s.Get_Robot()[s.getActiveGoal()];

                    Node node = new Node(manhattanDistance(newPosRobot, this.posActiveGoal) + cout, cout, s, n, move);

                    if (isInterestingNode(openList, closeList, node))
                    {
                        openList.add(node);
                    }
                }
            }
        }
        return null;
    }

}
