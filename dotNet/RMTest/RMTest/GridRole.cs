using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

public static class GridRole
{
    public enum GridRoleType
    {
        NOT_GRID,
        HUB,
        NODE
    }

    private static List<String> rcAliases()
    {
        List<String> values = new List<String>();

        values.Add("rc");
        values.Add("remotecontrol");
        values.Add("remote-control");
        return values;
    }

    private static List<String> wdAliases()
    {
        List<String> values = new List<String>();

        values.Add("wd");
        values.Add("webdriver");
        return values;
    }

    private static List<String> nodeAliases()
    {
        List<String> values = new List<String>();

        values.Add("rc");
        values.Add("remotecontrol");
        values.Add("remote-control");
        values.Add("wd");
        values.Add("webdriver");
        return values;
    }

    public static GridRoleType find(String[] args)
    {
        if (args == null)
        {
            return GridRoleType.NOT_GRID;
        }
        for (int i = 0; i < args.Length; i++)
        {
            if ("-role".Equals(args[i]))
            {
                if (i == args.Length - 1)
                {
                    return new GridRoleType();  // null;
                }
                else
                {
                    String role = args[i + 1].ToLower();
                    if (nodeAliases().Contains(role))
                    {
                        return GridRoleType.NODE;
                    }
                    else if ("hub".Equals(role))
                    {
                        return GridRoleType.HUB;
                    }
                    else if ("standalone".Equals(role))
                    {
                        return GridRoleType.NOT_GRID;
                    }
                    else
                    {
                        return new GridRoleType(); // null;
                    }
                }
            }
        }
        return GridRoleType.NOT_GRID;
    }
}

/*
private static List<String> wdAliases = new ImmutableList.Builder<String>()
  .add("wd")
  .add("webdriver")
  .build();

private static List<String> nodeAliases = new ImmutableList.Builder<String>()
  .add("node")
  .addAll(rcAliases)
  .addAll(wdAliases)
  .build();
*/

/**
 * finds the requested role from the parameters.
 *
 * @param args
 * @return the role in the grid from the -role param
 */

/*
public static GridRole find(String[] args)
{
    if (args == null)
    {
        return NOT_GRID;
    }
    for (int i = 0; i < args.length; i++)
    {
        if ("-role".equals(args[i]))
        {
            if (i == args.length - 1)
            {
                return null;
            }
            else
            {
                String role = args[i + 1].toLowerCase();
                if (nodeAliases.contains(role))
                {
                    return NODE;
                }
                else if ("hub".equals(role))
                {
                    return HUB;
                }
                else if ("standalone".equals(role))
                {
                    return NOT_GRID;
                }
                else
                {
                    return null;
                }
            }
        }
    }
    return NOT_GRID;
}

public static boolean isRC(String nodeType)
{
    return rcAliases.contains(nodeType);
}

public static boolean isWebDriver(String nodeType)
{
    return wdAliases.contains(nodeType);
}
}



namespace RMTest.openqa.grid.common
{
    class GridRole
    {
    }
}
*/