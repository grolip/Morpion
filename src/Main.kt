import kotlin.math.abs

const val joueurX: Char = 'X'
const val joueurO: Char = 'O'
const val caseVide: Char = '_'

class Morpion(taille: Int){
    private val tailleLigne = taille
    private var nCasesVides: Int = 0
    private var nCoupsX: Int = 0
    private var nCoupsO: Int = 0
    private var victoireX: Boolean = false
    private var victoireO: Boolean = false
    private var joueurActuel = joueurX
    private var grille: MutableList<MutableList<Char>> =
        MutableList(tailleLigne) { MutableList(tailleLigne) { caseVide } }

    private fun affGrille() {
        println("---------")
        for (i in 0..<tailleLigne){
            println("| " + grille[i].joinToString(" ") + " |")
        }
        println("---------")
    }

    private fun genGrilleVirtuelle(): MutableList<MutableList<Char>> {
        val grilleVirtuelle: MutableList<MutableList<Char>> = mutableListOf()

        // Récupération lignes horizontales
        for (i in 0..<tailleLigne){
            grilleVirtuelle.add(grille[i])
        }
        // Récupération lignes verticales
        for (i in 0..<tailleLigne){
            grilleVirtuelle.add(mutableListOf())
            grille.forEach { grilleVirtuelle.last().add(it[i]) }
        }
        // Récupération ligne diagonale gauche
        grilleVirtuelle.add(mutableListOf())
        for (i in 0..<tailleLigne){
            grilleVirtuelle.last().add(grille[i][i])
        }
        // Récupération ligne diagonale droite
        grilleVirtuelle.add(mutableListOf())
        for (i in 0..<tailleLigne){
            grilleVirtuelle.last().add(grille[i][tailleLigne-1-i])
        }
        return grilleVirtuelle
    }

    private fun majCompteur(){
        nCasesVides = 0
        nCoupsX = 0
        nCoupsO = 0

        grille.forEach { ligne ->
            nCasesVides += ligne.count { it == caseVide }
            nCoupsX += ligne.count { it == joueurX }
            nCoupsO += ligne.count { it == joueurO }
        }
    }

    private fun majStatut(){
        majCompteur()
        genGrilleVirtuelle().forEach { ligne ->
            // Détermine s'il y a une victoire sur la ligne
            if (ligne.count { it == joueurX } == tailleLigne) victoireX = true
            if (ligne.count { it == joueurO } == tailleLigne) victoireO = true
        }
    }

    fun affStatut(){
        if ((victoireO && victoireX) || abs(nCoupsX - nCoupsO) > 1)
            println("Impossible")
        else if (victoireX)
            println("X wins")
        else if (victoireO)
            println("O wins")
        else if (nCasesVides == 0)
            println("Draw")
        else
            println("Game not finished")
    }

    private fun changerDeJoueur(){
        joueurActuel = when(joueurActuel){
            joueurO -> joueurX
            else -> joueurO
        }
    }

    fun nouvellePartie(){
        if (nCasesVides != tailleLigne*tailleLigne)
            grille = MutableList(tailleLigne) { MutableList(tailleLigne) { caseVide } }
        majStatut()
        affGrille()
    }

    fun partieFinie(): Boolean {
        return victoireO || victoireX || nCasesVides == 0
    }

    fun jouer(x: Int, y: Int) {
        if (x in 1..tailleLigne && y in 1..tailleLigne) {
            if (grille[x-1][y-1] == caseVide){
                grille[x-1][y-1] = joueurActuel
                majStatut()
                changerDeJoueur()
                affGrille()
            }
            else println("This cell is occupied! Choose another one!")
        } else {
            println("Coordinates should be from 1 to $tailleLigne!")
        }
    }
}

fun main() {
    val morpion = Morpion(3)

    morpion.nouvellePartie()

    while(!morpion.partieFinie()) {
        try {
            val choixCase = readln().split(" ", limit = 2).map { it.trim().toInt() }
            morpion.jouer(choixCase[0], choixCase[1])
        } catch (e: NumberFormatException) {
            println("You should enter numbers!")
        }
    }
    morpion.affStatut()
}
