.data

.balign 4
i: .word 0

.balign 4
v: .skip 12

.balign 4
z: .skip 12

.text
.global main

main:
	push {lr}

start_if0:
	ldr r0, addr_i
	ldr r0, [r0, +#0]
	mov r1, #3
	cmp r0, r1
	bge end_if0

if0:
rep1:
	ldr r0, =rformat
	ldr r2, addr_z
	mov r5, #4
	ldr r4, addr_i
	ldr r4, [r4]
	mov r6, #3
	cmp r4, r6
	blt continue0
	ldr r0, =eformat0
	bl printf
	b end
continue0:
	mul r4, r4, r5
	mov r3, r4
	mov r4, #0
	add r3, r3, r4
	add r2, r2, r3
	mov r1, r2
	bl scanf

	ldr r0, addr_i
	push {r0}
	ldr r1, addr_i
	ldr r1, [r1, +#0]
	add r1, r1, #1
	push {r1}
	pop {r1}
	pop {r0}
	str r1, [r0, +#0]

	ldr r0, addr_i
	ldr r0, [r0, +#0]
	mov r1, #3
	cmp r0, r1
	blt rep1
	b end_if0
end_if0:

	ldr r0, addr_i
	push {r0}
	mov r1, #0
	pop {r0}
	str r1, [r0, +#0]

start_if2:
	ldr r0, addr_i
	ldr r0, [r0, +#0]
	mov r1, #3
	cmp r0, r1
	bge end_if2

if2:
rep3:
	ldr r0, =wformat
	mov r4, #4
	ldr r3, addr_i
	ldr r3, [r3]
	mov r5, #3
	cmp r3, r5
	blt continue1
	ldr r0, =eformat1
	bl printf
	b end
continue1:
	mul r3, r3, r4
	mov r2, r3
	mov r3, #0
	add r2, r2, r3
	ldr r1, addr_z
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, addr_i
	push {r0}
	ldr r1, addr_i
	ldr r1, [r1, +#0]
	add r1, r1, #1
	push {r1}
	pop {r1}
	pop {r0}
	str r1, [r0, +#0]

	ldr r0, addr_i
	ldr r0, [r0, +#0]
	mov r1, #3
	cmp r0, r1
	blt rep3
	b end_if2
end_if2:

	ldr r0, addr_i
	push {r0}
	mov r1, #0
	pop {r0}
	str r1, [r0, +#0]

	ldr r0, addr_v
	ldr r1, addr_z
	mov r2, #0
	mov r4, #4
copy_start4:
	cmp r2, #3
	beq copy_end4
	mul r3, r2, r4
	ldr r5, [r1, +r3]
	str r5, [r0, +r3]
	add r2, r2, #1
	b copy_start4
copy_end4:

start_if5:
	ldr r0, addr_i
	ldr r0, [r0, +#0]
	mov r1, #3
	cmp r0, r1
	bge end_if5

if5:
rep6:
	ldr r0, =wformat
	mov r4, #4
	ldr r3, addr_i
	ldr r3, [r3]
	mov r5, #3
	cmp r3, r5
	blt continue2
	ldr r0, =eformat2
	bl printf
	b end
continue2:
	mul r3, r3, r4
	mov r2, r3
	mov r3, #0
	add r2, r2, r3
	ldr r1, addr_v
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, addr_i
	push {r0}
	ldr r1, addr_i
	ldr r1, [r1, +#0]
	add r1, r1, #1
	push {r1}
	pop {r1}
	pop {r0}
	str r1, [r0, +#0]

	ldr r0, addr_i
	ldr r0, [r0, +#0]
	mov r1, #3
	cmp r0, r1
	blt rep6
	b end_if5
end_if5:

	ldr r0, addr_i
	push {r0}
	mov r1, #0
	pop {r0}
	str r1, [r0, +#0]

start_if7:
	ldr r0, addr_i
	ldr r0, [r0, +#0]
	mov r1, #3
	cmp r0, r1
	bge end_if7

if7:
rep8:
	ldr r0, =rformat
	ldr r2, addr_v
	mov r5, #4
	ldr r4, addr_i
	ldr r4, [r4]
	mov r6, #3
	cmp r4, r6
	blt continue3
	ldr r0, =eformat3
	bl printf
	b end
continue3:
	mul r4, r4, r5
	mov r3, r4
	mov r4, #0
	add r3, r3, r4
	add r2, r2, r3
	mov r1, r2
	bl scanf

	ldr r0, addr_i
	push {r0}
	ldr r1, addr_i
	ldr r1, [r1, +#0]
	add r1, r1, #1
	push {r1}
	pop {r1}
	pop {r0}
	str r1, [r0, +#0]

	ldr r0, addr_i
	ldr r0, [r0, +#0]
	mov r1, #3
	cmp r0, r1
	blt rep8
	b end_if7
end_if7:

	ldr r0, addr_i
	push {r0}
	mov r1, #0
	pop {r0}
	str r1, [r0, +#0]

start_if9:
	ldr r0, addr_i
	ldr r0, [r0, +#0]
	mov r1, #3
	cmp r0, r1
	bge end_if9

if9:
rep10:
	ldr r0, =wformat
	mov r4, #4
	ldr r3, addr_i
	ldr r3, [r3]
	mov r5, #3
	cmp r3, r5
	blt continue4
	ldr r0, =eformat4
	bl printf
	b end
continue4:
	mul r3, r3, r4
	mov r2, r3
	mov r3, #0
	add r2, r2, r3
	ldr r1, addr_v
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, addr_i
	push {r0}
	ldr r1, addr_i
	ldr r1, [r1, +#0]
	add r1, r1, #1
	push {r1}
	pop {r1}
	pop {r0}
	str r1, [r0, +#0]

	ldr r0, addr_i
	ldr r0, [r0, +#0]
	mov r1, #3
	cmp r0, r1
	blt rep10
	b end_if9
end_if9:

	ldr r0, addr_i
	push {r0}
	mov r1, #0
	pop {r0}
	str r1, [r0, +#0]

start_if11:
	ldr r0, addr_i
	ldr r0, [r0, +#0]
	mov r1, #3
	cmp r0, r1
	bge end_if11

if11:
rep12:
	ldr r0, =wformat
	mov r4, #4
	ldr r3, addr_i
	ldr r3, [r3]
	mov r5, #3
	cmp r3, r5
	blt continue5
	ldr r0, =eformat5
	bl printf
	b end
continue5:
	mul r3, r3, r4
	mov r2, r3
	mov r3, #0
	add r2, r2, r3
	ldr r1, addr_z
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, addr_i
	push {r0}
	ldr r1, addr_i
	ldr r1, [r1, +#0]
	add r1, r1, #1
	push {r1}
	pop {r1}
	pop {r0}
	str r1, [r0, +#0]

	ldr r0, addr_i
	ldr r0, [r0, +#0]
	mov r1, #3
	cmp r0, r1
	blt rep12
	b end_if11
end_if11:

end:
	pop {pc}

wformat: .asciz "%d\n"
rformat: .asciz "%d"

eformat0: .asciz "Index identifier<i>@(331, 331) is out of bounds. Size of Array z is 3\n"
eformat1: .asciz "Index identifier<i>@(458, 458) is out of bounds. Size of Array z is 3\n"
eformat2: .asciz "Index identifier<i>@(642, 642) is out of bounds. Size of Array v is 3\n"
eformat3: .asciz "Index identifier<i>@(754, 754) is out of bounds. Size of Array v is 3\n"
eformat4: .asciz "Index identifier<i>@(884, 884) is out of bounds. Size of Array v is 3\n"
eformat5: .asciz "Index identifier<i>@(1004, 1004) is out of bounds. Size of Array z is 3\n"

addr_i : .word i
addr_v: .word v
addr_z: .word z
