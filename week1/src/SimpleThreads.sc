//:paste
private var uidCount = 0L

def getUniqueId(): Long = {
  uidCount = uidCount + 1
  uidCount
}

def startThread() = {
  val t = new Thread {
    override def run(): Unit = {
      val uids = for (i <- 0 until 1000000) yield getUniqueId()
      println(uids.max)
    }
  }
  t.start()
  t
}

startThread();startThread()

//synchroniZed version
//:paste
private val x = new AnyRef {}
private var uidCount2 = 0L

def getUniqueId2(): Long = x.synchronized {
  uidCount2 = uidCount2 + 1
  uidCount2
}

def startThread2() = {
  val t = new Thread {
    override def run(): Unit = {
      val uids = for (i <- 0 until 1000000) yield getUniqueId2()
      println(uids.max)
    }
  }
  t.start()
  t
}

startThread2();startThread2()
