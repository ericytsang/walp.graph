import java.util.*

/**
 * Created by Eric on 2/21/2016.
 */
data class SimpleEdge(override val src:Any,override val dst:Any):Edge

object SimpleEdgeFactory:EdgeFactory<SimpleEdge>
{
    override fun make(src:Any,dst:Any):SimpleEdge = SimpleEdge(src,dst)
}

class SimpleGraph():MutableGraph<String,SimpleEdge>()
{
    override val edgeFactory:EdgeFactory<SimpleEdge> = SimpleEdgeFactory
    override val adjacencyList:MutableMap<String,MutableSet<String>> = LinkedHashMap()
    override val nodeMap:MutableMap<Any,String> = LinkedHashMap()
    override val edgeMap:MutableMap<Pair<String,String>,SimpleEdge> = LinkedHashMap()

    override fun toString():String
    {
        val s = StringBuilder()
        adjacencyList.map {it.key}.forEachIndexed()
        {
            i, node ->
            if (i != 0) s.append('\n')
            s.append(node)
            s.append(" : ")
            s.append(getNeighbors(node))
        }
        return s.toString()
    }
}
