/* sample2 - a sample module -- module for /proc with no read/write functions */

#include <linux/version.h>
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/module.h>
#include <linux/fs.h>
#include <linux/proc_fs.h>

MODULE_AUTHOR("Bob Russell <rdr@unh.edu>");
MODULE_DESCRIPTION("CS721/821 sample2");
MODULE_LICENSE("GPL");


/*	static storage used to save this module's /proc registration address */
static struct proc_dir_entry *entry = NULL;


/*	function to initialize this module when it is loaded
 *	Returns 0 if everything worked ok
 *		-EXXXX error code if something went wrong
 */
static int __init
sample_init_module_function(void)
{

	printk("sample2: up and running ok\n");
	/* create a new read-only file in the top-level "/proc" directory */
	entry = create_proc_entry("sample2", S_IRUGO, NULL);
	if (entry) {
		printk("sample2: entry %p added to /proc\n", entry);
		return 0;	/* everything ok */
	} else {
		printk("sample2: add to /proc FAILED!\n");
		return -ENOMEM;
	}
}

/*	function to clean up this module when it is unloaded */
static void __exit
sample_cleanup_module_function(void)
{
	printk("sample2: closing down now\n");
	if (entry) {
		remove_proc_entry("sample2", NULL);
		printk("sample2: entry %p removed from /proc\n", entry);
		entry = NULL;
	}
	printk("sample2: closed down ok\n");
}

module_init(sample_init_module_function);
module_exit(sample_cleanup_module_function);

/* vi: set autoindent tabstop=8 shiftwidth=8 : */
