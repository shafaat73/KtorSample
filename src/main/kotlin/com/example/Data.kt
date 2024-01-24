package com.example

fun twoSum(nums: IntArray, target: Int): IntArray {
    val targetHash = hashMapOf<Int,Int>()
    for(i in nums.indices){
        val num = target - nums[i]
        if (targetHash.containsKey(num)){
            return intArrayOf(i, targetHash[num]!!)
        }
        targetHash[nums[i]] = i
    }
    return IntArray(0)
}

fun productExceptSelf(nums: IntArray): IntArray {
    val n = nums.size
    val result = IntArray(n)

    // Calculate products of elements to the left of each element
    var leftProduct = 1
    for (i in 0 until n) {
        result[i] = leftProduct
        leftProduct *= nums[i]
    }

    // Calculate products of elements to the right of each element and multiply with the left product
    var rightProduct = 1
    for (i in n - 1 downTo 0) {
        result[i] *= rightProduct
        rightProduct *= nums[i]
    }

    return result
}

fun main() {
    val nums = intArrayOf(1, 2, 3, 4)
    val position = nums.firstOrNull { it == 5 }
    println(position)
//    val result = productExceptSelf(nums)
//    println("Product of Array Except Self: ${result.joinToString()}")
}
