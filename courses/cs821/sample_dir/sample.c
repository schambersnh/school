/* sample - a trivial sample module -- just the module stuff */

#include <linux/version.h>
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/module.h>

MODULE_AUTHOR("Bob Russell <rdr@unh.edu>");
MODULE_DESCRIPTION("CS721/821 sample");
MODULE_LICENSE("GPL");


/*	function to initialize this module when it is loaded
 *	Returns 0 if everything worked ok
 *		-EXXXX error code if something went wrong
 */
static int __init
sample_init_module_function(void)
{
	printk("sample: up and running ok\n");
	return 0;	/* everything ok */
}

/*	function to clean up this module when it is unloaded */
static void __exit
sample_cleanup_module_function(void)
{
	printk("sample: closing down now\n");
}

module_init(sample_init_module_function);
module_exit(sample_cleanup_module_function);

/* vi: set autoindent tabstop=8 shiftwidth=8 : */
