class MyTest{
    fun main(args: Array<String>) {
        val list = mutableListOf("a", "b", "c", "d", "e")
        list.remove("a")//这里会报错, 通过remove函数注解定义，这个remove函数在定义的level是ERROR级别的，所以编译器直接抛错
    }
}