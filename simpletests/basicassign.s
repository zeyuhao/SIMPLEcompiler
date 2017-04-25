.data

.balign 4
arr: .skip 16

.balign 4
arr2: .skip 32

.balign 4
c: .word 0

.text
.global main

main:
	push {lr}

	ldr r0, =rformat
	ldr r2, addr_c
	add r2, r2, #0
	mov r1, r2
	bl scanf

	ldr r0, =wformat
	ldr r1, addr_c
	ldr r1, [r1, +#0]
	bl printf

	mov r3, #0
	mov r2, r3
	mov r4, #4
	ldr r3, addr_c
	ldr r3, [r3]
	mov r5, #2
	cmp r3, r5
	blt continue0
	ldr r0, =eformat0
	bl printf
	b end
continue0:
	mul r3, r3, r4
	add r2, r2, r3
	ldr r0, addr_arr
	push {r0}
	mov r1, #47
	pop {r0}
	str r1, [r0, +r2]

	ldr r0, =wformat
	mov r3, #0
	mov r2, r3
	mov r4, #4
	ldr r3, addr_c
	ldr r3, [r3]
	mov r5, #2
	cmp r3, r5
	blt continue1
	ldr r0, =eformat1
	bl printf
	b end
continue1:
	mul r3, r3, r4
	add r2, r2, r3
	ldr r1, addr_arr
	ldr r1, [r1, +r2]
	bl printf

	ldr r0, =wformat
	mov r3, #8
	mov r2, r3
	mov r4, #4
	ldr r3, addr_c
	ldr r3, [r3]
	mov r5, #2
	cmp r3, r5
	blt continue2
	ldr r0, =eformat2
	bl printf
	b end
continue2:
	mul r3, r3, r4
	add r2, r2, r3
	ldr r1, addr_arr
	ldr r1, [r1, +r2]
	mov r4, #0
	mov r3, r4
	mov r5, #4
	ldr r4, addr_c
	ldr r4, [r4]
	mov r6, #2
	cmp r4, r6
	blt continue3
	ldr r0, =eformat3
	bl printf
	b end
continue3:
	mul r4, r4, r5
	add r3, r3, r4
	ldr r2, addr_arr
	ldr r2, [r2, +r3]
	add r1, r1, r2
	push {r1}
	pop {r1}
	bl printf

	mov r4, #16
	ldr r3, addr_c
	ldr r3, [r3]
	mov r5, #2
	cmp r3, r5
	blt continue4
	ldr r0, =eformat4
	bl printf
	pop {r1}
	b end
continue4:
	mul r3, r3, r4
	mov r2, r3
	mov r3, #4
	add r2, r2, r3
	ldr r0, addr_arr2
	push {r0}
	mov r1, #47
	pop {r1}
	pop {r0}
	str r1, [r0, +r2]

	ldr r0, =rformat
	ldr r2, addr_c
	add r2, r2, #0
	mov r1, r2
	bl scanf

	ldr r0, =wformat
	mov r4, #16
	ldr r3, addr_c
	ldr r3, [r3]
	mov r5, #2
	cmp r3, r5
	blt continue5
	ldr r0, =eformat5
	bl printf
	b end
continue5:
	mul r3, r3, r4
	mov r2, r3
	mov r3, #4
	add r2, r2, r3
	ldr r1, addr_arr2
	ldr r1, [r1, +r2]
	bl printf

end:
	pop {pc}

wformat: .asciz "%d\n"
rformat: .asciz "%d"

eformat0: .asciz "Index identifier<c>@(178, 178) is out of bounds. Size of Array arr[0] is 2\n"
eformat1: .asciz "Index identifier<c>@(202, 202) is out of bounds. Size of Array arr[0] is 2\n"
eformat2: .asciz "Index identifier<c>@(221, 221) is out of bounds. Size of Array arr[1] is 2\n"
eformat3: .asciz "Index identifier<c>@(233, 233) is out of bounds. Size of Array arr[0] is 2\n"
eformat4: .asciz "Index identifier<c>@(244, 244) is out of bounds. Size of Array arr2 is 2\n"
eformat5: .asciz "Index identifier<c>@(278, 278) is out of bounds. Size of Array arr2 is 2\n"

addr_arr: .word arr
addr_arr2: .word arr2
addr_c : .word c
