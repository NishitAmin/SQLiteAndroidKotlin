package org.sheridan.sqliteandroidkotlindemo

class Product{
    var id: Int = 0
    var prodName: String? = null
    var quantity: String? = null

    constructor(name: String, quant: String){
        this.prodName = name
        this.quantity = quant
    }
}