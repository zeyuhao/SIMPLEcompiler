.data

.balign 4
p: .skip 34560

.balign 4
r: .skip 34560

.balign 4
s: .skip 32

.balign 4
v: .skip 240

.balign 4
z: .skip 240

.text
.global main

main:
	push {lr}

	ldr r0, =rformat
	ldr r2, addr_z
	mov r4, #140
	mov r3, r4
	mov r4, #20
	add r3, r3, r4
	mov r4, #20
	add r3, r3, r4
	mov r4, #0
	add r3, r3, r4
	mov r4, #16
	add r3, r3, r4
	add r2, r2, r3
	mov r1, r2
	bl scanf

	ldr r0, =wformat
	mov r3, #140
	mov r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #0
	add r2, r2, r3
	mov r3, #16
	add r2, r2, r3
	ldr r1, addr_z
	ldr r1, [r1, +r2]
	bl printf

	mov r3, #140
	mov r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #0
	add r2, r2, r3
	mov r3, #16
	add r2, r2, r3
	ldr r0, addr_v
	push {r0}
	mov r4, #140
	mov r3, r4
	mov r4, #20
	add r3, r3, r4
	mov r4, #20
	add r3, r3, r4
	mov r4, #0
	add r3, r3, r4
	mov r4, #16
	add r3, r3, r4
	ldr r1, addr_z
	ldr r1, [r1, +r3]
	pop {r0}
	str r1, [r0, +r2]

	ldr r0, =wformat
	mov r3, #140
	mov r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #0
	add r2, r2, r3
	mov r3, #16
	add r2, r2, r3
	ldr r1, addr_v
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, =rformat
	ldr r2, addr_v
	mov r4, #140
	mov r3, r4
	mov r4, #20
	add r3, r3, r4
	mov r4, #20
	add r3, r3, r4
	mov r4, #0
	add r3, r3, r4
	mov r4, #16
	add r3, r3, r4
	add r2, r2, r3
	mov r1, r2
	bl scanf

	ldr r0, =wformat
	mov r3, #140
	mov r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #0
	add r2, r2, r3
	mov r3, #16
	add r2, r2, r3
	ldr r1, addr_v
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, =wformat
	mov r3, #140
	mov r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #0
	add r2, r2, r3
	mov r3, #16
	add r2, r2, r3
	ldr r1, addr_z
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, =rformat
	ldr r2, addr_r
	ldr r4, =#20160
	mov r3, r4
	ldr r4, =#1680
	add r3, r3, r4
	mov r4, #140
	add r3, r3, r4
	mov r4, #20
	add r3, r3, r4
	mov r4, #20
	add r3, r3, r4
	mov r4, #0
	add r3, r3, r4
	mov r4, #16
	add r3, r3, r4
	add r2, r2, r3
	mov r1, r2
	bl scanf

	ldr r0, =wformat
	ldr r3, =#20160
	mov r2, r3
	ldr r3, =#1680
	add r2, r2, r3
	mov r3, #140
	add r2, r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #0
	add r2, r2, r3
	mov r3, #16
	add r2, r2, r3
	ldr r1, addr_r
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, addr_r
	ldr r1, addr_p
	mov r2, #0
	mov r4, #4
copy_start0:
	cmp r2, #8640
	beq copy_end0
	mul r3, r2, r4
	ldr r5, [r1, +r3]
	str r5, [r0, +r3]
	add r2, r2, #1
	b copy_start0
copy_end0:

	ldr r0, =wformat
	ldr r3, =#20160
	mov r2, r3
	ldr r3, =#1680
	add r2, r2, r3
	mov r3, #140
	add r2, r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #20
	add r2, r2, r3
	mov r3, #0
	add r2, r2, r3
	mov r3, #16
	add r2, r2, r3
	ldr r1, addr_r
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, =rformat
	ldr r2, addr_s
	mov r4, #16
	mov r3, r4
	mov r4, #0
	add r3, r3, r4
	mov r4, #8
	add r3, r3, r4
	mov r4, #0
	add r3, r3, r4
	mov r4, #4
	add r3, r3, r4
	mov r4, #0
	add r3, r3, r4
	add r2, r2, r3
	mov r1, r2
	bl scanf

	ldr r0, =wformat
	mov r3, #16
	mov r2, r3
	mov r3, #0
	add r2, r2, r3
	mov r3, #8
	add r2, r2, r3
	mov r3, #0
	add r2, r2, r3
	mov r3, #4
	add r2, r2, r3
	mov r3, #0
	add r2, r2, r3
	ldr r1, addr_s
	ldr r1, [r1, +r2]
	bl printf

end:
	pop {pc}

wformat: .asciz "%d\n"
rformat: .asciz "%d"

addr_p: .word p
addr_r: .word r
addr_s: .word s
addr_v: .word v
addr_z: .word z
