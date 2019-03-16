package com.pantaubersama.app.data.model.tps

data class Logs(
    val calculation: Calculation,
    val form_c1: FormC1,
    val images: Images
)

data class Images(
    val dpd: Boolean,
    val dpr_ri: Boolean,
    val dprd_kabupaten: Boolean,
    val dprd_provinsi: Boolean,
    val presiden: Boolean,
    val suasana_tps: Boolean
)

data class FormC1(
    val dpd: Boolean,
    val dpr_ri: Boolean,
    val dprd_kabupaten: Boolean,
    val dprd_provinsi: Boolean,
    val presiden: Boolean
)

data class Calculation(
    val dpd: Boolean,
    val dpr_ri: Boolean,
    val dprd_kabupaten: Boolean,
    val dprd_provinsi: Boolean,
    val presiden: Boolean
)