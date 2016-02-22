import java.util.*

/**
 * Created by Eric on 2/21/2016.
 */
data class SimpleEdge(override val src:Node,override val dst:Node):Edge

data class StringNode(val name:String):Node
{
    override fun toString():String = name
}

object SimpleEdgeFactory:EdgeFactory<SimpleEdge>
{
    override fun make(src:Node,dst:Node):SimpleEdge = SimpleEdge(src,dst)
}

class SimpleGraph():MutableGraph<StringNode,SimpleEdge>
{
    override val edgeFactory:EdgeFactory<SimpleEdge> = SimpleEdgeFactory
    override val adjacencyList:MutableMap<StringNode,MutableSet<StringNode>> = LinkedHashMap()
    override val nodeMap:MutableMap<Any,StringNode> = LinkedHashMap()
    override val edgeMap:MutableMap<Pair<StringNode,StringNode>,SimpleEdge> = LinkedHashMap()

    override fun toString():String
    {
        val s = StringBuilder()
        adjacencyList.map {it.key}.forEach()
        {
            node ->
            s.append(node)
            s.append(" : ")
            s.append(getNeighbors(node))
            s.append('\n')
        }
        return s.toString()
    }
}
