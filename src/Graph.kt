import java.util.*

/**
 * Created by Eric on 2/21/2016.
 */
interface Node

interface Edge
{
    val src:Node
    val dst:Node
}

interface Graph<N:Node,E:Edge>
{
    val adjacencyList:Map<N,Set<N>>
    val nodeMap:Map<Any,N>
    val edgeMap:Map<Pair<N,N>,E>
}

// misc
fun <N:Node,E:Edge> Graph<N,E>.getNeighbors(node:N):Set<N> = adjacencyList[node] ?: throw IllegalArgumentException("node does not exist")
fun <N:Node,E:Edge> Graph<N,E>.getEdges(node:N):Set<E> = getNeighbors(node).map {get(node,it) ?: throw IllegalStateException("edgeMap is missing an edge")}.toSet()

// nodes
fun <N:Node,E:Edge> Graph<N,E>.get(key:Any):N? = nodeMap[key]
fun <N:Node,E:Edge> Graph<N,E>.has(key:Any):Boolean = get(key) != null
val <N:Node,E:Edge> Graph<N,E>.nodes:Set<N> get() = adjacencyList.keys

// edges
fun <N:Node,E:Edge> Graph<N,E>.get(src:Node,dst:Node):E? = edgeMap[Pair(src,dst)]
fun <N:Node,E:Edge> Graph<N,E>.has(src:Node,dst:Node):Boolean = get(src,dst) != null
val <N:Node,E:Edge> Graph<N,E>.edges:Set<E> get() = edgeMap.values.toSet()

interface MutableGraph<N:Node,E:Edge>:Graph<N,E>
{
    val edgeFactory:EdgeFactory<E>
    override val adjacencyList:MutableMap<N,MutableSet<N>>
    override val nodeMap:MutableMap<Any,N>
    override val edgeMap:MutableMap<Pair<N,N>,E>
}

interface EdgeFactory<E:Edge>
{
    fun make(src:Node,dst:Node):E
}

// nodes
fun <N:Node,E:Edge> MutableGraph<N,E>.put(node:N):N?
{
    adjacencyList.put(node,LinkedHashSet())
    return nodeMap.put(node,node)
}
fun <N:Node,E:Edge> MutableGraph<N,E>.remove(node:N):N?
{
    adjacencyList.remove(node)
    adjacencyList.values.forEach {it.removeAll {it == node}}
    edgeMap.entries.removeAll {it.value.src == node || it.value.dst == node}
    return nodeMap.remove(node)
}
fun <N:Node,E:Edge> MutableGraph<N,E>.getOrPut(node:N):N
{
    var result = get(node)
    if (result == null)
    {
        put(node)
        result = get(node)
    }
    return result!!
}

// edges
fun <N:Node,E:Edge> MutableGraph<N,E>.put(src:N,dst:N):E?
{
    adjacencyList.getOrPut(src,{LinkedHashSet()}).add(dst)
    return edgeMap.put(Pair(src,dst),edgeFactory.make(src,dst))
}
fun <N:Node,E:Edge> MutableGraph<N,E>.remove(src:N,dst:N):E?
{
    adjacencyList[src]?.remove(dst)
    return edgeMap.remove(Pair(src,dst))
}
fun <N:Node,E:Edge> MutableGraph<N,E>.getOrPut(src:N,dst:N):E
{
    var result = get(src,dst)
    if (result == null)
    {
        put(src,dst)
        result = get(src,dst)
    }
    return result!!
}
