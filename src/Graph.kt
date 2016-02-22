/**
 * Created by Eric on 2/21/2016.
 */
interface Node
{
    val name:String
    val edges:Set<Edge>
}

val Node.neighbors:Set<Node> get() = edges.map {it.dst}.toSet()

interface Edge
{
    val src:Node
    val dst:Node
}

interface Graph<N:Node,E:Edge>
{
    val edges:Set<Edge>
    fun put(src:N,dst:N):E
    fun get(src:N,dst:N):E?
    fun remove(src:N,dst:N):E
    val nodes:Map<String,Node>
    fun put(name:String):N
    fun get(name:String):N?
    fun remove(name:String):N
}

fun <N:Node,E:Edge> Graph<N,E>.has(src:N,dst:N):Boolean = get(src,dst) != null
fun <N:Node,E:Edge> Graph<N,E>.getOrPut(src:N,dst:N):E = get(src,dst) ?: put(src,dst)

fun <N:Node,E:Edge> Graph<N,E>.has(name:String):Boolean = get(name) != null
fun <N:Node,E:Edge> Graph<N,E>.getOrPut(name:String):N = get(name) ?: put(name)
