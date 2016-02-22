import java.util.*

/**
 * Created by Eric on 2/21/2016.
 */
interface Edge
{
    val src:Any
    val dst:Any
}

interface EdgeFactory<E:Edge>
{
    fun make(src:Any,dst:Any):E
}

abstract class Graph<N:Any,E:Edge>
{
    abstract protected val adjacencyList:Map<N,Set<N>>
    abstract protected val nodeMap:Map<Any,N>
    abstract protected val edgeMap:Map<Pair<N,N>,E>

    // misc
    fun getNeighbors(node:N):Set<N> = adjacencyList[node] ?: throw IllegalArgumentException("node does not exist")

    // nodes
    fun get(key:Any):N? = nodeMap[key]
    val nodes:Set<N> get() = adjacencyList.keys

    // edges
    fun get(src:Any,dst:Any):E? = edgeMap[Pair(src,dst)]
    val edges:Set<E> get() = edgeMap.values.toSet()
}

fun <N:Any,E:Edge> Graph<N,E>.getEdges(node:N):Set<E> = getNeighbors(node).map {get(node,it) ?: throw IllegalStateException("edgeMap is missing an edge")}.toSet()
fun <N:Any,E:Edge> Graph<N,E>.has(key:Any):Boolean = get(key) != null
fun <N:Any,E:Edge> Graph<N,E>.has(src:Any,dst:Any):Boolean = get(src,dst) != null

abstract class MutableGraph<N:Any,E:Edge>:Graph<N,E>()
{
    abstract protected val edgeFactory:EdgeFactory<E>
    abstract override val adjacencyList:MutableMap<N,MutableSet<N>>
    abstract override val nodeMap:MutableMap<Any,N>
    abstract override val edgeMap:MutableMap<Pair<N,N>,E>

    // nodes
    fun put(node:N):N? = synchronized(this)
    {
        adjacencyList.getOrPut(node,{LinkedHashSet()})
        return nodeMap.put(node,node)
    }

    fun remove(node:N):N? = synchronized(this)
    {
        adjacencyList.remove(node)
        adjacencyList.values.forEach {it.removeAll {it == node}}
        edgeMap.entries.removeAll {it.value.src == node || it.value.dst == node}
        return nodeMap.remove(node)
    }

    // edges
    fun put(src:N,dst:N):E? = synchronized(this)
    {
        getOrPut(src)
        getOrPut(dst)
        adjacencyList.getOrPut(src,{LinkedHashSet()}).add(dst)
        return edgeMap.put(Pair(src,dst),edgeFactory.make(src,dst))
    }

    fun remove(src:N,dst:N):E? = synchronized(this)
    {
        adjacencyList[src]?.remove(dst)
        return edgeMap.remove(Pair(src,dst))
    }
}

fun <N:Any,E:Edge> MutableGraph<N,E>.getOrPut(node:N):N = synchronized(this)
{
    var result = get(node)
    if (result == null)
    {
        put(node)
        result = get(node)
    }
    return result!!
}

fun <N:Any,E:Edge> MutableGraph<N,E>.getOrPut(src:N,dst:N):E = synchronized(this)
{
    var result = get(src,dst)
    if (result == null)
    {
        put(src,dst)
        result = get(src,dst)
    }
    return result!!
}
