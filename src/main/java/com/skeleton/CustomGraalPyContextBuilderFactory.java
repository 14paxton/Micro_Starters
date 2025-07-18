package com.skeleton;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Value;
import io.micronaut.graal.graalpy.GraalPyContextBuilderFactory;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.python.embedding.GraalPyResources;
import org.graalvm.python.embedding.VirtualFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@io.micronaut.context.annotation.Context
@Replaces(GraalPyContextBuilderFactory.class)
public class CustomGraalPyContextBuilderFactory implements GraalPyContextBuilderFactory {
  private static final Logger LOG = LoggerFactory.getLogger(CustomGraalPyContextBuilderFactory.class);
  private final VirtualFileSystem vfs;
  private final static Engine engine = Engine.create();


  public CustomGraalPyContextBuilderFactory(@Value("${graalpy.vfs.resource-directory}") String resourceDirectory) {
    this.vfs = VirtualFileSystem.newBuilder()
                                .resourceDirectory(resourceDirectory)
                                .build();
  }

  @Override
  public Context.Builder createBuilder() {

    return GraalPyResources.contextBuilder(vfs)
                           .engine(engine)
                           .allowNativeAccess(true)
                           .allowCreateProcess(true)
                           .allowExperimentalOptions(true)
                           .allowCreateThread(true)
                           .useSystemExit(true)
                           .option("python.VerboseFlag", "false")
                           .option("python.NativeModules", "true")
                           .option("python.IsolateNativeModules", "false");
  }
}

/*

Option: python.AlwaysRunExcepthook - This option is set by the Python launcher to tell the language it can print
exceptions directly

Option: python.AttributeAccessInlineCacheMaxDepth -

Option: python.BackgroundGCTask - Whether the background GC task should be enabled (default) or not.

Option: python.BackgroundGCTaskInterval - Specifies the interval (ms) for the background GC task to monitor the
resident set size (RSS)

Option: python.BackgroundGCTaskMinimum - The minimum RSS memory (in megabytes) to start calling System.gc().
(default: 4 GB).

Option: python.BackgroundGCTaskThreshold - The percentage increase in RSS memory between System.gc() calls. Low
percentage will trigger System.gc() more often. (default: 30).

Option: python.BaseExecutable - The sys._base_executable path. Set by the launcher, but may need to be overridden in
certain special situations.

Option: python.BuiltinsInliningMaxCallerSize - Stop inlining of builtins if caller's cumulative tree size
        would exceed this limit

Option: python.CAPI - Set the location of C API home. Overrides any environment variables or Java options.

Option: python.CallSiteInlineCacheMaxDepth -

Option: python.CatchAllExceptions - Enable catching all Exceptions in generic try-catch statements.

Option: python.CatchGraalPythonExceptionForUnitTesting -

Option: python.CheckHashPycsMode - Value of the --check-hash-based-pycs command line option- 'default' means the
'check_source' flag in hash-based pycs  determines invalidation- 'always' causes the interpreter to hash the source
file for  invalidation regardless of value of 'check_source' bit- 'never' causes the interpreter to always assume

hash-based pycs are  validThe default value is 'default'.See PEP 552 'Deterministic pycs' for more details.

Option: python.CoreHome - Set the location of what is usually lib/graalpy<graalvm_major>.<graalvm_minor>. Overrides
any environment variables or Java options.

Option: python.DisableFrozenModules - Disables using frozen modules.

Option: python.DontWriteBytecodeFlag - Equivalent to the Python -B flag. Don't write bytecode files.

Option: python.EagerlyMaterializeInstrumentationNodes - Makes bytecode instrumentation node materialization eager
instead of lazy.

Option: python.EmulateJython - Emulate some Jython features that can cause performance degradation

Option: python.EnableDebuggingBuiltins - Enable built-in functions on the __graalpython__ module that are useful for
debugging.

Option: python.EnableForcedSplits - Enable forced splitting (of builtins). Default false.

Option: python.Executable - The sys.executable path. Set by the launcher, but may need to be overridden in certain
special situations.

Option: python.ExecutableList - The executed command list as string joined by the executable list separator
        char. This must always correspond to the real, valid command list used to run GraalPy.

Option: python.ExposeInternalSources - Expose internal sources as normal sources, so they will show up in the
         debugger and stacks

Option: python.ForceImportSite - Force to automatically import site.py module.

Option: python.ForceInitializeSourceSections - Eagerly initialize source sections.

Option: python.ForceInlineGeneratorCalls -

Option: python.ForceSharingForInnerContexts - Forces AST sharing for inner contexts.

Option: python.HPyBackend - Choose the backend for HPy binary mode.

Option: python.HPyEnableJNIFastPaths - If {@code true}, code is enabled that tries to reduce expensive
        upcalls into the runtimewhen HPy API functions are used. This is achieved by mirroring data in native memory.

Option: python.HPyTraceUpcalls - Specifies the interval (ms) for periodically printing HPy upcall statistics.
         If {@code 0}or not specified, nothing will be printed (default).

Option: python.HashSeed - Equivalent to setting PYTHONHASHSEED environment variable

Option: python.IgnoreEnvironmentFlag - Equivalent to the Python -E flag. Ignore PYTHON* environment variables.

Option: python.InitialLocale - Sets the language and territory, which will be used for initial locale. Format:
'language[_territory]', e.g., 'en_GB'. Leave empty to use the JVM default locale.

Option: python.InitialNativeMemory - Initial native memory heap size that triggers a GC (default: 256 MB).

Option: python.InputFilePath - Used by the launcher to pass the path to be executed

Option: python.InspectFlag - Equivalent to the Python -i flag. Inspect interactively after running a script.

Option: python.InstallSignalHandlers - Install default signal handlers on startup

Option: python.IntMaxStrDigits - Equivalent to the Python -X int_max_str_digits option.

Option: python.IsolateFlag - Equivalent to the Python -I flag. Isolate from the users environment by not
        adding the cwd to the path

Option: python.IsolateNativeModules - Whether the context should isolate its loading of C extension modules. This
allows more than one context to access C extensions. Note that all contexts in the operating system process must set
this option to true to cooperatively allow this feature to work.

Option: python.JNIHome - Specify the directory where the JNI library is located.

Option: python.LazyStrings - Switch on/off using lazy strings for performance reasons. Default true.

Option: python.MaxNativeMemory - Max native memory heap size (default: 8 GB).

Option: python.NativeModules - Whether C extension modules should be loaded as native code (as opposed to LLVM
bitcode execution).

Option: python.NoAsyncActions - Disable weakref callback processing, signal handling, and other periodic async actions.

Option: python.NoSiteFlag - Equivalent to the Python -S flag. Don't imply 'import site' on initialization.

Option: python.NoUserSiteFlag - Equivalent to the Python -s flag. Don't add user site directory to sys.path.

Option: python.NodeRecursionLimit -

Option: python.OrigArgv - The list of the original command line arguments passed to the Python executable.

Option: python.OverallocateLiteralLists - Propagate append operations to lists created as literals back to
        where they were created, to inform overallocation to avoid having to grow them later.

Option: python.ParserLogFiles - Prints path to parsed files

Option: python.ParserStatistics - Prints parser time statistics after number of parsed files, set by this option. 0
or <0 means no statistics are printed.

Option: python.PosixModuleBackend - Choose the backend for the POSIX module.

Option: python.PyCachePrefix - If this is set, GraalPy will write .pyc files in a mirror directory tree at this path,
 instead of in __pycache__ directories within the source tree. Equivalent to setting the PYTHONPYCACHEPREFIX
 environment variable for the standard launcher.

Option: python.PythonGC - Whether the Python GC should be enabled (default) or not.

Option: python.PythonHome - Set the home of Python. Equivalent of GRAAL_PYTHONHOME env variable. Determines
        default values for the CoreHome, StdLibHome, SysBasePrefix, SysPrefix.

Option: python.PythonOptimizeFlag - Remove assert statements and any code conditional on the value of __debug__.

Option: python.PythonPath - Equivalent to setting the PYTHONPATH environment variable for the standard
        launcher. ':'-separated list of directories prefixed to the default module search path.

Option: python.QuietFlag - Equivalent to the Python -q flag. Don't  print version and copyright messages on
        interactive startup.

Option: python.RunViaLauncher - Set by the launcher to true (false means that GraalPy is being embedded in an
         application).

Option: python.SafePathFlag - Equivalent to the Python -P flag. Don't prepend a potentially unsafe path to
        sys.path

Option: python.Sha3ModuleBackend - Choose the backend for the Sha3 module.

Option: python.StandardStreamEncoding - Equivalent to setting the PYTHONIOENCODING environment variable for
        the standard launcher.

Option: python.StdLibHome - Set the location of lib/python3.11. Overrides any environment variables or Java
        options.

Option: python.SysBasePrefix - Set the location of sys.base_prefix. Overrides any environment variables or
        Java options.

Option: python.SysPrefix - Set the location of sys.prefix. Overrides any environment variables or Java options.

Option: python.TRegexUsesSREFallback - Use the CPython sre engine as a fallback to the TRegex engine.

Option: python.TerminalHeight - Set by the launcher to the terminal height.

Option: python.TerminalIsInteractive - Set by the launcher if an interactive console is used to run Python.

Option: python.TerminalWidth - Set by the launcher to the terminal width.

Option: python.TraceNativeMemory - Enable tracing of native memory (ATTENTION: this will have significant
        impact on CExt execution performance).

Option: python.TraceNativeMemoryCalls - If native memory tracing is enabled, also capture stack.

Option: python.UnbufferedIO - Equivalent to the Python -u flag. Force stdout and stderr to be unbuffered.

Option: python.UseNativePrimitiveStorageStrategy - If true, uses native storage strategy for primitive types

Option: python.UsePanama - Use the experimental panama backend for NFI.

Option: python.UseReprForPrintString - Embedder option: what to print in response to PythonLanguage#toString.

Option: python.UseSystemToolchain - If true, use the system's toolchain for native extension compilation.
        Otherwise, use the LLVM Toolchain included with GraalVM.

Option: python.VariableArgumentInlineCacheLimit -

Option: python.VariableArgumentReadUnrollingLimit -

Option: python.VenvlauncherCommand - Option used by the venvlauncher to pass on the launcher target command

Option: python.VerboseFlag - Equivalent to the Python -v flag. Turn on verbose mode.

Option: python.WarnDefaultEncodingFlag - Equivalent to the Python -X warn_default_encoding flag. Enable
        opt-in EncodingWarning for 'encoding=None'

Option: python.WarnExperimentalFeatures - Print warnings when using experimental features at runtime.

Option: python.WarnOptions - Equivalent to setting the PYTHONWARNINGS environment variable for the standard
        launcher.

Option: python.WithCachedSources - Determines wether context startup tries to re-use previously cached
        sources of the core library.

Option: python.WithJavaStacktrace - Print the Java stacktrace for exceptions. Possible modes:    0   Do not
        print any Java stacktraces.    1   Print Java stacktrace for Java exceptions only (default).    2   Print
        Java stacktrace for Python exceptions only (ATTENTION: this will have a notable performance impact).    3
        Combines 1 and 2.

Option: python.WithTRegex - Use the optimized TRegex engine. Default true
        */
