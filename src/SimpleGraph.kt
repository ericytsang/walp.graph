import java.util.*

/**
 * Created by Eric on 2/21/2016.
 */
data class SimpleEdge(override val src:Node,override val dst:Node):Edge

class SimpleNode(override val name:String,internal val _edges:MutableSet<Edge> = LinkedHashSet()):Node
{
    override val edges:Set<Edge> get() = _edges
    override fun toString():String = name
}

class SimpleGraph:Graph<Node,Edge>
{
    private val _edges:MutableMap<Edge,Edge> = LinkedHashMap()
    override val edges:Set<Edge> get() = _edges.keys
    override fun put(src:Node,dst:Node):Edge
    {
        if (has(src,dst)) throw IllegalArgumentException("edge($src,$dst) already exists")
        val edge = SimpleEdge(src,dst)
        _edges.put(edge,edge)
        (src as SimpleNode)._edges.add(edge)
        return edge
    }
    override fun get(src:Node,dst:Node):Edge? = _edges[SimpleEdge(src,dst)]
    override fun remove(src:Node,dst:Node):Edge
    {
        val edge = get(src,dst) ?: throw IllegalArgumentException("edge($src,$dst) does not exist")
        (src as SimpleNode)._edges.remove(edge)
        _edges.remove(edge)
        return edge
    }

    private val _nodes:MutableMap<String,Node> = LinkedHashMap()
    override val nodes:Map<String,Node> get() = _nodes
    override fun put(name:String):Node
    {
        if (has(name)) throw IllegalArgumentException("node($name) already exists")
        val node = SimpleNode(name)
        _nodes.put(name,node)
        return node
    }
    override fun get(name:String):Node? = _nodes[name]
    override fun remove(name:String):Node
    {
        val node = get(name) ?: throw IllegalArgumentException("node($name) does not exist")
        node.edges.forEach {remove(it.src,it.dst)}
        _nodes.remove(name)
        return node
    }

    override fun toString():String
    {
        val s = StringBuilder()
        nodes.map {it.value}.forEach()
        {
            node ->
            s.append(node)
            s.append(" : ")
            s.append(node.neighbors)
            s.append('\n')
        }
        return s.toString()
    }
}
