using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Group13SSIS.Models;

namespace Group13SSIS.Utility
{
    public static class UsernameVerification
    {
        public static bool IsUsernameExist(string username)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var user = db.Users.FirstOrDefault(a => a.Username == username);
                return user != null;
            }
        }
    }
}