#include <linux/module.h>
#include <linux/vermagic.h>
#include <linux/compiler.h>

MODULE_INFO(vermagic, VERMAGIC_STRING);

#undef unix
struct module __this_module
__attribute__((section(".gnu.linkonce.this_module"))) = {
 .name = __stringify(KBUILD_MODNAME),
 .init = init_module,
#ifdef CONFIG_MODULE_UNLOAD
 .exit = cleanup_module,
#endif
};

static const struct modversion_info ____versions[]
__attribute_used__
__attribute__((section("__versions"))) = {
	{ 0xee8a4511, "struct_module" },
	{ 0xabe13280, "remove_proc_entry" },
	{ 0x1d26aa98, "sprintf" },
	{ 0x1b7d4074, "printk" },
	{ 0x4784e424, "__get_free_pages" },
	{ 0x3e1cf02d, "create_proc_entry" },
	{ 0x9941ccb8, "free_pages" },
};

static const char __module_depends[]
__attribute_used__
__attribute__((section(".modinfo"))) =
"depends=";


MODULE_INFO(srcversion, "3F3537611C744EFE88FF218");
