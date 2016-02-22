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
        val n = arrayOf(g.put("0"),g.put("1"),g.put("2"),g.put("3"))
        g.put(n[0],n[1])
        g.put(n[3],n[1])
        g.put(n[2],n[1])
        g.put(n[1],n[0])
        g.put(n[1],n[2])
        g.put(n[1],n[3])
        println(g)
    }
}
