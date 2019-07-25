using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Group13SSIS.Models;
using Group13SSIS.Utility;

namespace Group13SSIS.Controllers
{
    public class HomeController : Controller
    {

        // GET: Register Page
        [HttpGet]
        public ActionResult Register()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                List<Dept> depts = db.Depts.ToList();
                ViewData["depts"] = depts;
            }
            return View();
        }
        // POST: for Admin to add new user
        [HttpPost]
        public ActionResult Register(UserVM userVM)
        {
            using (Group13SSISEntities dc = new Group13SSISEntities())
            {
                List<Dept> depts = dc.Depts.ToList();
                ViewData["depts"] = depts;
            }
            if (ModelState.IsValid)
            {
                if (UsernameVerification.IsUsernameExist(userVM.Username))
                {
                    ModelState.AddModelError("Username", "Username already exist");
                    return View(userVM);
                }
                User user = new User()
                {
                    Username = userVM.Username,
                    Password = Crypto.Hash(userVM.Password),
                    Name = userVM.Name,
                    Email = userVM.Email,
                    DeptId = Int32.Parse(userVM.DeptId)
                };
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    db.Users.Add(user);
                    db.SaveChanges();
                    return View(new UserVM());
                }
            }
            return View();
        }


    }
}