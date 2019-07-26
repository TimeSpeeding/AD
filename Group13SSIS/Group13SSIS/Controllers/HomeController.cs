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
            using (Group13SSISEntities dc = new Group13SSISEntities())
            {
                ViewData["depts"] = dc.Depts.ToList();
                ViewData["roles"] = dc.Roles.ToList();
            }
            return View();
        }
        // POST: for Admin to add new user
        [HttpPost]
        public ActionResult Register(UserVM userVM)
        {
            using (Group13SSISEntities dc = new Group13SSISEntities())
            {
                ViewData["depts"] = dc.Depts.ToList();
                ViewData["roles"] = dc.Roles.ToList();
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
                    DeptId = Int32.Parse(userVM.DeptId),
                    RoleId = Int32.Parse(userVM.RoleId)
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
                    return RedirectToAction("index", "Home");
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