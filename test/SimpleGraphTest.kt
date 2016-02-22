import org.junit.Test

/**
 * Created by Eric on 2/21/2016.
 */
class SimpleGraphTest
{
    @Test
    fun toStringTest()
    {
        val g = SimpleGraph()
        val n = arrayOf("0","1","2","3")
        n.forEach {g.put(it)}
        g.put(n[0],n[1])
        g.put(n[3],n[1])
        g.put(n[2],n[1])
        g.put(n[1],n[0])
        g.put(n[1],n[2])
        g.put(n[1],n[3])
        println(g)
    }

    @Test
    fun removeNodeTest()
    {
        val g0 = SimpleGraph()
        val n0 = arrayOf("0","1","2","3")
        g0.put(n0[0],n0[1])
        g0.put(n0[3],n0[1])
        g0.put(n0[2],n0[1])
        g0.put(n0[2],n0[0])
        g0.put(n0[2],n0[3])
        g0.put(n0[1],n0[0])
        g0.put(n0[1],n0[2])
        g0.put(n0[1],n0[3])
        g0.remove("3")
        println(g0)
        println(g0.edges)

        val g1 = SimpleGraph()
        val n1 = arrayOf("0","1","2")
        g1.put(n1[0],n1[1])
        g1.put(n1[2],n1[1])
        g1.put(n1[2],n1[0])
        g1.put(n1[1],n1[0])
        g1.put(n1[1],n1[2])
        println(g1)
        println(g1.edges)

        assert(g1.toString() == g0.toString())
        assert(g1.edges.toString() == g0.edges.toString())
    }

    @Test
    fun removeEdgeTest()
    {
        val g0 = SimpleGraph()
        val n0 = arrayOf("0","1","2","3")
        n0.forEach {g0.put(it)}
        g0.put(n0[0],n0[1])
        g0.put(n0[3],n0[1])
        g0.put(n0[2],n0[1])
        g0.put(n0[2],n0[0])
        g0.put(n0[2],n0[3])
        g0.put(n0[1],n0[0])
        g0.put(n0[1],n0[2])
        g0.put(n0[1],n0[3])
        g0.remove(n0[3],n0[1])
        g0.remove(n0[2],n0[3])
        g0.remove(n0[1],n0[3])
        println(g0)
        println(g0.edges)

        val g1 = SimpleGraph()
        val n1 = arrayOf("0","1","2","3")
        n1.forEach {g1.put(it)}
        g1.put(n1[0],n1[1])
        g1.put(n1[2],n1[1])
        g1.put(n1[2],n1[0])
        g1.put(n1[1],n1[0])
        g1.put(n1[1],n1[2])
        println(g1)
        println(g1.edges)

        assert(g1.toString() == g0.toString())
        assert(g1.edges.toString() == g0.edges.toString())
    }
}
