import java.util.*

/**
 * Created by Eric on 2/21/2016.
 */
interface Edge
{
    val src:Any
    val dst:Any
}

interface Graph<N:Any,E:Edge>
{
    val adjacencyList:Map<N,Set<N>>
    val nodeMap:Map<Any,N>
    val edgeMap:Map<Pair<N,N>,E>
}

// misc
fun <N:Any,E:Edge> Graph<N,E>.getNeighbors(node:N):Set<N> = adjacencyList[node] ?: throw IllegalArgumentException("node does not exist")
fun <N:Any,E:Edge> Graph<N,E>.getEdges(node:N):Set<E> = getNeighbors(node).map {get(node,it) ?: throw IllegalStateException("edgeMap is missing an edge")}.toSet()

// nodes
fun <N:Any,E:Edge> Graph<N,E>.get(key:Any):N? = nodeMap[key]
fun <N:Any,E:Edge> Graph<N,E>.has(key:Any):Boolean = get(key) != null
val <N:Any,E:Edge> Graph<N,E>.nodes:Set<N> get() = adjacencyList.keys

// edges
fun <N:Any,E:Edge> Graph<N,E>.get(src:Any,dst:Any):E? = edgeMap[Pair(src,dst)]
fun <N:Any,E:Edge> Graph<N,E>.has(src:Any,dst:Any):Boolean = get(src,dst) != null
val <N:Any,E:Edge> Graph<N,E>.edges:Set<E> get() = edgeMap.values.toSet()

interface MutableGraph<N:Any,E:Edge>:Graph<N,E>
{
    val edgeFactory:EdgeFactory<E>
    override val adjacencyList:MutableMap<N,MutableSet<N>>
    override val nodeMap:MutableMap<Any,N>
    override val edgeMap:MutableMap<Pair<N,N>,E>
}

interface EdgeFactory<E:Edge>
{
    fun make(src:Any,dst:Any):E
}

// nodes
fun <N:Any,E:Edge> MutableGraph<N,E>.put(node:N):N?
{
    adjacencyList.getOrPut(node,{LinkedHashSet()})
    return nodeMap.put(node,node)
}
fun <N:Any,E:Edge> MutableGraph<N,E>.remove(node:N):N?
{
    // fixme: optimize this
    adjacencyList.remove(node)
    adjacencyList.values.forEach {it.removeAll {it == node}}
    edgeMap.entries.removeAll {it.value.src == node || it.value.dst == node}
    return nodeMap.remove(node)
}
fun <N:Any,E:Edge> MutableGraph<N,E>.getOrPut(node:N):N
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
fun <N:Any,E:Edge> MutableGraph<N,E>.put(src:N,dst:N):E?
{
    getOrPut(src)
    getOrPut(dst)
    adjacencyList.getOrPut(src,{LinkedHashSet()}).add(dst)
    return edgeMap.put(Pair(src,dst),edgeFactory.make(src,dst))
}
fun <N:Any,E:Edge> MutableGraph<N,E>.remove(src:N,dst:N):E?
{
    adjacencyList[src]?.remove(dst)
    return edgeMap.remove(Pair(src,dst))
}
fun <N:Any,E:Edge> MutableGraph<N,E>.getOrPut(src:N,dst:N):E
{
    var result = get(src,dst)
    if (result == null)
    {
        put(src,dst)
        result = get(src,dst)
    }
    return result!!
}
