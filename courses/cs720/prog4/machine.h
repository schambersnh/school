/*	machine.h	*/

enum opcodes	{
	illegal, next_if_soln, next_if_eoln, done, split, jump,
	match, matchset, matchany, opcode_limit
};

extern char	*opcode_names[opcode_limit];

/* define the opcode and operands for one action */
struct action {
	unsigned short	opcode;
	unsigned short	oprand;
};

/* define the "pattern maching machine" architecture */
struct machine {
	struct action	*actions;	/* read-only "program" */
	unsigned char	**brset_table;	/* table of bracket sets */
	int		action_limit;	/* no. of instructions in actions */
	int		first_printable;/* first printable character */
	int		last_printable; /* last printable character */
	int		brset_size;	/* no. of bytes in one brset */
	int		brset_limit;	/* no of brsets in brset_table */
	int		ignore_case;	/* treat upper/lower case identical */
};

/* define the 2 useful bits in flags parameter to recompiler */
#define	IGNORE_CASE	0x01
#define LIST_CODE	0x02

extern int recompiler(char *input_rexpr, int flags,
						struct machine **new_machine);

extern void free_machine(struct machine *old_machine);
