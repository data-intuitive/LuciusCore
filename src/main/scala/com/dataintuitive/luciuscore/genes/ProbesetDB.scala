package com.dataintuitive.luciuscore.genes

class ProbesetDB(probesets: Array[Probeset]) extends Serializable {

  val psidToSymbolsMap:Map[Probesetid, List[Symbol]] = probesets
    .map(gene => (gene.probesetID, gene.symbol))
    .flatMap{ case(psid, set) => set.map(el => (psid, el)) }
    .groupBy(_._1)
    .map{ case(psid, symbols) => (psid, symbols.map(_._2).toList)}

  val symbols = probesets.map(ps => ps.symbol).flatMap(x => x).distinct
  def indexedSymbols = 
    symbols
      .zipWithIndex
      .map{ case(k,i) => (k, i+1)}          // 1-based index
      .map{ case(k,v) => (v,k) }.toMap

  def toGeneDB = GeneDB(
    indexedSymbols
      .map{ case(index, symbol) => new Gene(index, symbol)}
      .map{ case Gene(index, symbol, _, _) =>
        val probesets = searchSymbol(symbol)
        Gene(index, symbol, probesets.map(_.probesetID), probesets.map(_.index))
      }
      .toArray
      )

  def lookup(psid: Probesetid) = probesets.filter(_.probesetID == psid).head

  def searchSymbol(symbol: Symbol):List[Probeset] = probesets.filter(_.symbol.contains(symbol)).toList

  val toTSV:String = probesets.map{ case Probeset(index, probesetID, dataType, entrezID, ensemblID, swissprotID, symbol, name, unigeneClusterType, chromosomalLocation, geneOntologyBiologicalProcess) =>
    index + "\t" + probesetID + "\t" + dataType + "\t" + entrezID.mkString(" // ") + "\t" + ensemblID.mkString("//") + "\t" + swissprotID.mkString("//") + "\t" + symbol.mkString("//") + "\t" + name + "\t" + unigeneClusterType + "\t" + chromosomalLocation + "\t" + geneOntologyBiologicalProcess
  }.mkString(
    "index \t probesetID \t dataType \t entrezID \t ensemblID \t swissprotID \t symbol \t name \t unigeneClusterType \t chromosomalLocation \t geneOntologyBiologicalProcess \n",
    "\n",
    "")

}
