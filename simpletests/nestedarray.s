.data

.balign 4
a: .skip 20

.balign 4
z: .word 0

.text
.global main

main:
	push {lr}

	ldr r0, =rformat
	ldr r2, addr_z
	add r2, r2, #0
	mov r1, r2
	bl scanf

	ldr r0, =rformat
	ldr r2, addr_a
	mov r5, #4
	ldr r4, addr_z
	ldr r4, [r4]
	mov r6, #5
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

	ldr r0, addr_z
	push {r0}
	mov r4, #4
	ldr r3, addr_z
	ldr r3, [r3]
	mov r5, #5
	cmp r3, r5
	blt continue1
	ldr r0, =eformat1
	bl printf
	pop {r0}
	b end
continue1:
	mul r3, r3, r4
	mov r2, r3
	mov r3, #0
	add r2, r2, r3
	ldr r1, addr_a
	ldr r1, [r1, +r2]
	mov r5, #4
	ldr r4, addr_z
	ldr r4, [r4]
	mov r6, #5
	cmp r4, r6
	blt continue2
	ldr r0, =eformat2
	bl printf
	pop {r0}
	b end
continue2:
	mul r4, r4, r5
	mov r3, r4
	mov r4, #0
	add r3, r3, r4
	ldr r2, addr_a
	ldr r2, [r2, +r3]
	mov r6, #4
	ldr r5, addr_z
	ldr r5, [r5]
	mov r7, #5
	cmp r5, r7
	blt continue3
	ldr r0, =eformat3
	bl printf
	pop {r0}
	b end
continue3:
	mul r5, r5, r6
	mov r4, r5
	mov r5, #0
	add r4, r4, r5
	ldr r3, addr_a
	ldr r3, [r3, +r4]
	mov r7, #4
	ldr r6, addr_z
	ldr r6, [r6]
	mov r8, #5
	cmp r6, r8
	blt continue4
	ldr r0, =eformat4
	bl printf
	pop {r0}
	b end
continue4:
	mul r6, r6, r7
	mov r5, r6
	mov r6, #0
	add r5, r5, r6
	ldr r4, addr_a
	ldr r4, [r4, +r5]
	mov r8, #4
	ldr r7, addr_z
	ldr r7, [r7]
	mov r9, #5
	cmp r7, r9
	blt continue5
	ldr r0, =eformat5
	bl printf
	pop {r0}
	b end
continue5:
	mul r7, r7, r8
	mov r6, r7
	mov r7, #0
	add r6, r6, r7
	ldr r5, addr_a
	ldr r5, [r5, +r6]
	mov r9, #4
	ldr r8, addr_z
	ldr r8, [r8]
	mov r10, #5
	cmp r8, r10
	blt continue6
	ldr r0, =eformat6
	bl printf
	pop {r0}
	b end
continue6:
	mul r8, r8, r9
	mov r7, r8
	mov r8, #0
	add r7, r7, r8
	ldr r6, addr_a
	ldr r6, [r6, +r7]
	mov r10, #4
	ldr r9, addr_z
	ldr r9, [r9]
	mov r11, #5
	cmp r9, r11
	blt continue7
	ldr r0, =eformat7
	bl printf
	pop {r0}
	b end
continue7:
	mul r9, r9, r10
	mov r8, r9
	mov r9, #0
	add r8, r8, r9
	ldr r7, addr_a
	ldr r7, [r7, +r8]
	mov r11, #4
	ldr r10, addr_z
	ldr r10, [r10]
	mov r12, #5
	cmp r10, r12
	blt continue8
	ldr r0, =eformat8
	bl printf
	pop {r0}
	b end
continue8:
	mul r10, r10, r11
	mov r9, r10
	mov r10, #0
	add r9, r9, r10
	ldr r8, addr_a
	ldr r8, [r8, +r9]
	add r7, r7, r8
	push {r7}
	mov r5, #4
	ldr r4, addr_z
	ldr r4, [r4]
	mov r6, #5
	cmp r4, r6
	blt continue9
	ldr r0, =eformat9
	bl printf
	pop {r0}
	pop {r7}
	b end
continue9:
	mul r4, r4, r5
	mov r3, r4
	mov r4, #0
	add r3, r3, r4
	ldr r2, addr_a
	ldr r2, [r2, +r3]
	pop {r7}
	add r7, r7, r2
	push {r7}
	pop {r7}
	add r6, r6, r7
	push {r6}
	pop {r6}
	add r5, r5, r6
	push {r5}
	pop {r5}
	add r4, r4, r5
	push {r4}
	pop {r4}
	add r3, r3, r4
	push {r3}
	pop {r3}
	add r2, r2, r3
	push {r2}
	pop {r2}
	add r1, r1, r2
	push {r1}
	pop {r1}
	pop {r0}
	str r1, [r0, +#0]

	ldr r0, =wformat
	ldr r1, addr_z
	ldr r1, [r1, +#0]
	bl printf

end:
	pop {pc}

wformat: .asciz "%d\n"
rformat: .asciz "%d"

eformat0: .asciz "Index identifier<z>@(104, 104) is out of bounds. Size of Array a is 5\n"
eformat1: .asciz "Index identifier<z>@(120, 120) is out of bounds. Size of Array a is 5\n"
eformat2: .asciz "Index identifier<z>@(130, 130) is out of bounds. Size of Array a is 5\n"
eformat3: .asciz "Index identifier<z>@(140, 140) is out of bounds. Size of Array a is 5\n"
eformat4: .asciz "Index identifier<z>@(150, 150) is out of bounds. Size of Array a is 5\n"
eformat5: .asciz "Index identifier<z>@(160, 160) is out of bounds. Size of Array a is 5\n"
eformat6: .asciz "Index identifier<z>@(170, 170) is out of bounds. Size of Array a is 5\n"
eformat7: .asciz "Index identifier<z>@(180, 180) is out of bounds. Size of Array a is 5\n"
eformat8: .asciz "Index identifier<z>@(189, 189) is out of bounds. Size of Array a is 5\n"
eformat9: .asciz "Index identifier<z>@(199, 199) is out of bounds. Size of Array a is 5\n"

addr_a: .word a
addr_z : .word z
