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
        
        

        [HttpGet]
        public ActionResult Login()
        {
            return View();
        }
        [HttpPost]
        public ActionResult Login(UserVM userVM)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {

                userVM.Password = Crypto.Hash(userVM.Password);
                var user = db.Users.Where(x => x.Username == userVM.Username && x.Password == userVM.Password).FirstOrDefault();
                if (user == null)
                {
                    ModelState.AddModelError("Password", "Username or password is incorrect");
                    userVM.Password = null;
                    return View(userVM);
                }
                else
                {
                    Session["user"] = user;
                    if (user.RoleId == 1) return RedirectToAction("UserList", "Admin");
                    else return RedirectToAction("index", "Home");
                }
            }

        }

        public ActionResult Index()
        {
            return View();
        }
        public ActionResult Logout()
        {
            var user = (User)Session["user"];
            Session.Abandon();
            return RedirectToAction("Login", "Home");
        }
        public ActionResult Sorry()
        {
            return View();
        }
    }
}