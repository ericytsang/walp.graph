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
    fun getEdges(node:N):Set<E> = getNeighbors(node).map {get(node,it) ?: throw IllegalStateException("edgeMap is missing an edge")}.toSet()

    // nodes
    fun get(key:Any):N? = nodeMap[key]
    fun has(key:Any):Boolean = get(key) != null
    val nodes:Set<N> get() = adjacencyList.keys

    // edges
    fun get(src:Any,dst:Any):E? = edgeMap[Pair(src,dst)]
    fun has(src:Any,dst:Any):Boolean = get(src,dst) != null
    val edges:Set<E> get() = edgeMap.values.toSet()
}

abstract class MutableGraph<N:Any,E:Edge>:Graph<N,E>()
{
    abstract protected val edgeFactory:EdgeFactory<E>
    abstract override val adjacencyList:MutableMap<N,MutableSet<N>>
    abstract override val nodeMap:MutableMap<Any,N>
    abstract override val edgeMap:MutableMap<Pair<N,N>,E>

    // nodes
    fun put(node:N):N?
    {
        adjacencyList.getOrPut(node,{LinkedHashSet()})
        return nodeMap.put(node,node)
    }

    fun remove(node:N):N?
    {
        // fixme: optimize this
        adjacencyList.remove(node)
        adjacencyList.values.forEach {it.removeAll {it == node}}
        edgeMap.entries.removeAll {it.value.src == node || it.value.dst == node}
        return nodeMap.remove(node)
    }

    fun getOrPut(node:N):N
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
    fun put(src:N,dst:N):E?
    {
        getOrPut(src)
        getOrPut(dst)
        adjacencyList.getOrPut(src,{LinkedHashSet()}).add(dst)
        return edgeMap.put(Pair(src,dst),edgeFactory.make(src,dst))
    }

    fun remove(src:N,dst:N):E?
    {
        adjacencyList[src]?.remove(dst)
        return edgeMap.remove(Pair(src,dst))
    }

    fun getOrPut(src:N,dst:N):E
    {
        var result = get(src,dst)
        if (result == null)
        {
            put(src,dst)
            result = get(src,dst)
        }
        return result!!
    }
}